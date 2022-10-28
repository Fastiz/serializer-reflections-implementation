import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
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

    fun <T : Any> tokenize(obj: T?): Token {
        if (obj == null) {
            return NullToken
        }

        val clazz = obj::class
        if (primitiveClasses.contains(clazz)) {
            return PrimitiveToken(obj)
        }

        val members = (clazz as KClass<Any>).declaredMemberProperties
            .map {
                val name = it.name
                val value = it.get(obj)
                val token = tokenize(value)
                Pair(name, token)
            }

        return NestedToken(members)
    }
}