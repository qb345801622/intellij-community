// IS_APPLICABLE: true
fun foo() {
    val x = <caret>bar("x", 0)
}

fun <T, V> bar(t: T, v: V): Int = 1