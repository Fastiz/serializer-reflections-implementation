import kotlin.test.Test
import kotlin.test.assertEquals

class JSONSerializerTest {

    @Test
    fun serialize() {
        class A(val field1: String, val field2: Int, val field3: A?)

        val nestedObj = A("value1", 2, null)
        val obj = A("value1", 3, nestedObj)

        val expectedJSON = ("{" +
                "field1: value1," +
                "field2: 3," +
                "field3: {" +
                "   field1: value1," +
                "   field2: 2," +
                "   field3: null" +
                "   }" +
                "}")
            .filter { !it.isWhitespace() }

        val tokenizer = Tokenizer()
        val serializer = JSONSerializer(tokenizer)

        val result = serializer.serialize(obj)

        assertEquals(expectedJSON, result)
    }
}