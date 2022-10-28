import kotlin.test.Test
import kotlin.test.assertEquals

class JSONSerializerTest {

    @Test
    fun serialize() {
        class A(val field1: String, val field2: Int, val field3: A?, val field4: List<Any?>)

        val nestedObj = A("value1", 2, null, listOf<Any?>("a", 1, "b", "c", null))
        val obj = A("value1", 3, nestedObj, emptyList())

        val expectedJSON = (
                "{" +
                "   field1: \"value1\"," +
                "   field2: 3," +
                "   field3: {" +
                "      field1: \"value1\"," +
                "      field2: 2," +
                "      field3: null," +
                "      field4: [\"a\", 1, \"b\", \"c\", null]" +
                "   }," +
                "   field4: []" +
                "}"
                )
            .filter { !it.isWhitespace() }

        val serializer = JSONSerializer()

        val result = serializer.serialize(obj)

        assertEquals(expectedJSON, result)
    }
}