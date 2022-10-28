import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TokenizerTest {
    @Test
    fun `when the object is null return a null token`() {
        val tokenizer = Tokenizer()
        val obj = null

        val token = tokenizer.tokenize(obj)

        assertTrue { token is NullToken }
    }

    @Test
    fun `when the object is a primitive return a primitive token`() {
        val tokenizer = Tokenizer()
        val obj = 1

        val token = tokenizer.tokenize(obj)

        assertTrue { token is PrimitiveToken<*> }
        if (token !is PrimitiveToken<*>) return

        assertEquals(obj, token.value)
    }

    @Test
    fun `when the object is a non primitive class then return a nested token`() {
        data class A(val a: String, val b: Int)

        val tokenizer = Tokenizer()
        val obj = A("hola", 2)

        val token = tokenizer.tokenize(obj)

        assertTrue { token is NestedToken }
        if (token !is NestedToken) return

        val aToken = token.members.find { it.first == "a" }
        assertNotNull(aToken)
        assertTrue { aToken.second is PrimitiveToken<*> }
        val aTokenToken = aToken.second
        if (aTokenToken !is PrimitiveToken<*>) return
        assertEquals("hola", aTokenToken.value)

        val bToken = token.members.find { it.first == "b" }
        assertNotNull(bToken)
        assertTrue { bToken.second is PrimitiveToken<*> }
        val bTokenToken = bToken.second
        if (bTokenToken !is PrimitiveToken<*>) return
        assertEquals(2, bTokenToken.value)
    }
}