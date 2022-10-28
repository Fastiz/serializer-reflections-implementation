sealed interface Token
class PrimitiveToken<T>(val value: T) : Token
class NestedToken(val members: List<Pair<String, Token>>) : Token
object NullToken : Token

class Tokenizer {
    private val primitiveClasses = listOf(
        String::class,
        Int::class,
        Double::class,
        Float::class,
        Long::class,
    )

    fun <T> tokenize(obj: T): Token {
        if (obj == null) {
            return NullToken
        }

        val clazz = obj!!::class
        if (primitiveClasses.contains(clazz)) {
            return PrimitiveToken(obj)
        }

        val members = clazz.members
            .filter { it.parameters.size == 1 }
            // FIXME: find a better way
            .filter { it.parameters[0].name == null }
            .map {
                val name = it.name
                val token = tokenize(it.call(obj))
                Pair(name, token)
            }

        return NestedToken(members)
    }
}