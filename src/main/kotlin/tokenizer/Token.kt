package tokenizer

sealed interface Token

class PrimitiveToken<T>(val value: T) : Token

class NestedToken(val members: List<Pair<String, Token>>) : Token

class ListToken(val elements: List<Token>) : Token

object NullToken : Token
