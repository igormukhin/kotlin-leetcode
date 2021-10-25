package olympia

import java.lang.IllegalArgumentException
import java.util.*

/**
 * [Problem 2](http://neerc.ifmo.ru/school/archive/1995-1996/ru-olymp-roi-1996-problems.html)
 * of Russian national programming competition, 1995-1996.
 */
fun main() {
   val source = """
       10 N=5
       20 SUM=SUM+N
       35 N=N-1
       40 IF N > 0 THEN WALKTO 20
       70 TYPE Sum
       999 END
    """.trimIndent()

    val nodes = parsePsiProgram(source)
    //nodes.forEach { println(it) }

    val vars = extractVars(nodes)
    //println(vars)

    val beta = translateToBeta(vars, nodes)
    beta.forEach { println(it) }
}

private fun translateToBeta(
    vars: Set<String>,
    nodes: List<Node>
): MutableList<String> {
    val beta = mutableListOf<String>()
    val csVars = vars.joinToString(separator = ", ")
    val stateVar = createStateVar(vars)
    beta.add("DEF $csVars, $stateVar;")
    beta.add("START")
    vars.forEach { beta.add("  $it = 0;") }
    beta.add("  $stateVar = 1;")
    beta.add("  WHILE $stateVar <> 0 DO")

    val betaVisitor = BetaVisitor({ beta.add(it) }, { lbl -> nodes.indexOfFirst { it.label == lbl } + 1 }, stateVar)
    nodes.forEachIndexed { nodeIndex, node ->
        val state = nodeIndex + 1
        beta.add("    IF $stateVar == $state THEN $stateVar := $stateVar + 1;")
        betaVisitor.handle(node)
        beta.add("    FI;")
    }

    beta.add("  OD;")
    beta.add("FINISH")
    return beta
}

fun createStateVar(vars: Set<String>): String {
    for (i in 0..9999999) {
       val v = "S" + i.toString().map { ch -> ch + ('A' - '0') }.joinToString(separator = "")
        if (v !in vars) return v
    }
    throw RuntimeException("can find var for state")
}

private fun parsePsiProgram(source: String): List<Node> {
    val lineRegex = Regex("\\s*(\\d+)\\s*(.+)")
    val codeRegex = Regex("([a-zA-Z]+)(\\s*(=)?(.+))?")
    val ifRegex = Regex("(.+)\\s+THEN\\s+(.+)")

    fun parseNode(label: Int, code: String): Node {
        //println(code)
        val codeMatch = codeRegex.matchEntire(code)!!
        val firstToken = codeMatch.groups[1]!!.value
        val maybeEquals = codeMatch.groups[3]?.value
        val tail = codeMatch.groups[4]?.value

        if (firstToken == "END") {
            return EndNode(label)
        } else if (firstToken == "TYPE") {
            return PrintNode(label, tail!!.trim())
        } else if (firstToken == "WALKTO") {
            return GotoNode(label, tail!!.toInt())
        } else if (firstToken == "IF") {
            val ifMatch = ifRegex.matchEntire(tail!!)!!
            val condition = ifMatch.groups[1]!!.value
            val thenNode = parseNode(0, ifMatch.groups[2]!!.value)
            return IfNode(label, condition, thenNode)
        } else if (maybeEquals == "=") {
            return AssignNode(label, firstToken, tail!!.trim())
        } else {
            throw IllegalArgumentException("Invalid code: $code")
        }
    }

    val nodes = source.lineSequence().map { it.trim() }.map { line ->
        val lineMatch = lineRegex.matchEntire(line)!!
        val label = (lineMatch.groups[1]!!.value).toInt()
        val tail = lineMatch.groups[2]!!.value
        parseNode(label, tail)
    }.toList()
    return nodes
}

private fun extractVars(nodes: List<Node>): Set<String> {
    val varRegex = Regex("[a-zA-Z]+")
    fun vars(expr: String): List<String> {
        return varRegex.findAll(expr).map { it.value }.toList()
    }

    return nodes.asSequence()
                .map { it.expressions() }
                .flatMap { it.asSequence() }
                .map { vars(it) }
                .flatMap { it.asSequence() }
                .map { it.uppercase(Locale.getDefault()) }
                .toSet()
}

private sealed interface Node {
    val label: Int
        get() = 0

    fun expressions(): List<String> = listOf()
}
private data class EndNode(override val label: Int) : Node
private data class PrintNode(override val label: Int, val expr: String) : Node {
    override fun expressions(): List<String> = listOf(expr)
}
private data class GotoNode(override val label: Int, val target: Int) : Node
private data class AssignNode(override val label: Int, val target: String, val expr: String) : Node {
    override fun expressions(): List<String> = listOf(target, expr)
}
private data class IfNode(override val label: Int, val condition: String, val thenNode: Node) : Node {
    override fun expressions(): List<String> = listOf(condition) + thenNode.expressions()
}

private abstract class NodeVisitor {
    private var indent: Int = 6
    protected fun indent() { indent += 2 }
    protected fun unindent() { indent -= 2 }
    protected fun prefix(): String = " ".repeat(indent)

    fun handle(node: Node) {
        when (node) {
            is AssignNode -> visit(node)
            is GotoNode -> visit(node)
            is PrintNode -> visit(node)
            is EndNode -> visit(node)
            is IfNode -> visit(node)
        }
    }
    abstract fun visit(node: EndNode)
    abstract fun visit(node: PrintNode)
    abstract fun visit(node: GotoNode)
    abstract fun visit(node: AssignNode)
    abstract fun visit(node: IfNode)
}

private class BetaVisitor(
        val output: (String) -> Unit,
        val label2state: (Int) -> Int,
        val stateVar: String) : NodeVisitor() {

    private fun out(str: String) {
        output("${prefix()}$str")
    }

    override fun visit(node: EndNode) {
        out("$stateVar := 0;")
    }

    override fun visit(node: PrintNode) {
        out("PRINT (${node.expr});")
    }

    override fun visit(node: GotoNode) {
        out("$stateVar := ${label2state(node.target)};")
    }

    override fun visit(node: AssignNode) {
        out("${node.target} := ${node.expr};")
    }

    override fun visit(node: IfNode) {
        out("IF ${node.condition} THEN")
        indent()
        handle(node.thenNode)
        unindent()
        out("FI;")
    }

}

