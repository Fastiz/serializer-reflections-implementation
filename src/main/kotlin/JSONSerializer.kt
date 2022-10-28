import tokenizer.ListToken
import tokenizer.NestedToken
import tokenizer.NullToken
import tokenizer.PrimitiveToken
import tokenizer.Token
import tokenizer.Tokenizer

class JSONSerializer {
    private val tokenizer: Tokenizer = Tokenizer.buildTokenizer {
        addPrimitiveClass<Int>()
        addPrimitiveClass<String>()
        addPrimitiveClass<Boolean>()
    }

    fun <T> serialize(obj: T): String {
        val token = tokenizer.tokenize(obj)

        return mapTokenToJSON(token)
    }

    private fun mapTokenToJSON(token: Token): String {
        return when (token) {
            is NullToken -> mapNullTokenToJSON()
            is PrimitiveToken<*> -> mapPrimitiveTokenToJSON(token)
            is NestedToken -> mapNestedTokenToJSON(token)
            is ListToken -> mapListTokenToJSON(token)
        }
    }

    private fun mapListTokenToJSON(token: ListToken): String {
        val elements = token.elements.joinToString(",") { mapTokenToJSON(it) }
        return "[$elements]"
    }

    private fun mapNullTokenToJSON(): String {
        return "null"
    }

    private fun mapPrimitiveTokenToJSON(token: PrimitiveToken<*>): String {
        if (token.value is String) {
            return "\"${token.value}\""
        }

        if (token.value is Boolean) {
            return if (token.value) "true" else "false"
        }

        if (token.value is Int) {
            return "${token.value}"
        }

        throw IllegalStateException("mapPrimitiveTokenToJSON - unexpected primitive type")
    }

    private fun mapNestedTokenToJSON(token: NestedToken): String {
        val members = token.members.joinToString(",") { "${it.first}:${mapTokenToJSON(it.second)}" }
        return "{$members}"
    }
}