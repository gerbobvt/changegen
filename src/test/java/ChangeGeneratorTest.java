import org.junit.Test;
import org.stevens.changegen.ChangeBuilder;
import org.stevens.changegen.ChangeBuilderImpl;
import org.stevens.changegen.ChangeGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChangeGeneratorTest
{
    @Test
    public void OriginalAndCurrentPropertiesMatchReturnsNothing()
    {
        ChangeBuilder<TestObject, String> builder = new ChangeBuilderImpl<>();
        builder.AddSelectorProperty(TestObject::getId, (o, c) -> "Test Failed");

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(1, "Current");

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals(0, result.size());
    }

    @Test
    public void OriginalAndCurrentPropertiesDoNotMatchReturnsResult()
    {
        ChangeBuilder<TestObject, String> builder = new ChangeBuilderImpl<>();
        builder.AddSelectorProperty(TestObject::getId, (o, c) -> String.format("Original Id '%d' changed to '%d'", o.getId(), c.getId()));

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(2, "Current");

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals("Original Id '1' changed to '2'", result.get(0));
    }

    @Test
    public void OriginalAndCurrentListPropertiesMatchReturnsNothing()
    {
        ChangeBuilder<TestObject, String> builder = new ChangeBuilderImpl<>();
        builder.AddListProperty(TestObject::getListOfInts, k -> k, a -> "Test Failed", r -> "Test Failed", null);

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(1, "Current");

        List<Integer> ints1 = new ArrayList<>(5);
        ints1.add(1);
        ints1.add(2);
        ints1.add(3);
        ints1.add(4);
        ints1.add(5);

        List<Integer> ints2 = new ArrayList<>(5);
        ints2.add(1);
        ints2.add(2);
        ints2.add(3);
        ints2.add(4);
        ints2.add(5);

        originalObject.getListOfInts().addAll(ints1);
        currentObject.getListOfInts().addAll(ints2);

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals(0, result.size());
    }

    @Test
    public void AddItemsToListPropertyReturnsResult()
    {

    }

    class TestObject
    {
        private final int id;
        private final String name;
        private final List<Integer> listOfInts = new ArrayList<>();

        public TestObject(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Integer> getListOfInts() {
            return listOfInts;
        }
    }
}
