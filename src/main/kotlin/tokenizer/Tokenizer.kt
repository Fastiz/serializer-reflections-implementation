package tokenizer

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class Tokenizer(private val primitiveClasses: List<KClass<*>>) {
    fun <T : Any> tokenize(obj: T?): Token {
        if (obj == null) {
            return NullToken
        }

        val clazz = obj::class
        if (primitiveClasses.contains(clazz)) {
            return PrimitiveToken(obj)
        }

        if (obj is List<*>) {
            val elements = obj.map { tokenize(it) }
            return ListToken(elements)
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

    companion object {
        class TokenizerBuilder {
            private val primitiveClasses = mutableListOf<KClass<*>>()
            inline fun <reified T> addPrimitiveClass() {
                addPrimitiveClass(clazz = T::class)
            }

            fun addPrimitiveClass(clazz: KClass<*>) {
                primitiveClasses.add(clazz)
            }

            fun build(): Tokenizer = Tokenizer(primitiveClasses)
        }

        fun buildTokenizer(executor: TokenizerBuilder.() -> Unit): Tokenizer {
            val tokenizerBuilder = TokenizerBuilder()
            tokenizerBuilder.executor()
            return tokenizerBuilder.build()
        }
    }
}