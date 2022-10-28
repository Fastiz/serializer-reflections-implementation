class JSONSerializer(private val tokenizer: Tokenizer) {
    fun <T> serialize(obj: T): String {
        val token = tokenizer.tokenize(obj)

        return mapTokenToJSON(token)
    }

    private fun mapTokenToJSON(token: Token): String {
        return when(token){
            is NullToken -> mapNullTokenToJSON()
            is PrimitiveToken<*> -> mapPrimitiveTokenToJSON(token)
            is NestedToken -> mapNestedTokenToJSON(token)
        }
    }

    private fun mapNullTokenToJSON(): String {
        return "null"
    }

    private fun mapPrimitiveTokenToJSON(token: PrimitiveToken<*>): String {
        return token.value.toString()
    }

    private fun mapNestedTokenToJSON(token: NestedToken): String {
        val members = token.members.joinToString(",") { "${it.first}:${mapTokenToJSON(it.second)}" }
        return "{$members}"
    }
}