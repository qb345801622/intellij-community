// PARAM_TYPES: kotlin.Int
// PARAM_TYPES: kotlin.Int
// PARAM_DESCRIPTOR: value-parameter a: kotlin.Int defined in foo
// PARAM_DESCRIPTOR: val b: kotlin.Int defined in foo
// SIBLING:
fun foo(a: Int) {
    val b: Int = 1
    for (n in 1..b) {
        <selection>if (a > 0) throw Exception("")
        if (a + b > 0) break
        println(a - b)
        return</selection>
    }
}

// IGNORE_K2