import org.junit.Test;
import org.stevens.changegen.ChangeBuilder;
import org.stevens.changegen.ChangeGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChangeGeneratorTest
{
    @Test
    public void OriginalAndCurrentPropertiesMatchReturnsNothing()
    {
        ChangeBuilder<TestObject, String> builder = ChangeBuilder.getNewBuilder();
        builder.addSelectorProperty(TestObject::getId, (o, c) -> "Test Failed");

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(1, "Current");

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals(0, result.size());
    }

    @Test
    public void OriginalAndCurrentPropertiesDoNotMatchReturnsResult()
    {
        ChangeBuilder<TestObject, String> builder = ChangeBuilder.getNewBuilder();
        builder.addSelectorProperty(TestObject::getId, (o, c) -> String.format("Original Id '%d' changed to '%d'", o.getId(), c.getId()));

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(2, "Current");

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals("Original Id '1' changed to '2'", result.get(0));
    }

    @Test
    public void CurrentListHasAddedElement()
    {
        ChangeBuilder<TestObject, String> builder = ChangeBuilder.getNewBuilder();
        builder.addListProperty(TestObject::getListOfInts, k -> k, a -> "Test Failed", r -> "Test Failed", null);

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(1, "Current");

        List<Integer> ints1 = new ArrayList<>(5);
        ints1.add(1);

        List<Integer> ints2 = new ArrayList<>(5);
        ints2.add(1);
        ints2.add(2);

        originalObject.getListOfInts().addAll(ints1);
        currentObject.getListOfInts().addAll(ints2);

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals(1, result.size());
    }

    @Test
    public void CurrentListHasRemovedElement()
    {
        ChangeBuilder<TestObject, String> builder = ChangeBuilder.getNewBuilder();
        builder.addListProperty(TestObject::getListOfInts, k -> k, a -> "Test Failed", r -> "Test Failed", null);

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        TestObject originalObject = new TestObject(1, "Original");
        TestObject currentObject = new TestObject(1, "Current");

        List<Integer> ints1 = new ArrayList<>(5);
        ints1.add(1);
        ints1.add(2);
        ints1.add(3);

        List<Integer> ints2 = new ArrayList<>(5);
        ints2.add(1);
        ints2.add(2);

        originalObject.getListOfInts().addAll(ints1);
        currentObject.getListOfInts().addAll(ints2);

        List<String> result = generator.getChanges(originalObject, currentObject);

        assertEquals(1, result.size());
    }

    @Test
    public void ListItemHasChange()
    {
        ChangeBuilder<TestSubObject, String> subBuilder = ChangeBuilder.getNewBuilder();
        subBuilder.addSelectorProperty(s -> s.subName, (o, c) -> String.format("Subname changed from '%s' to '%s'", o.subName, c.subName));

        ChangeBuilder<TestObject, String> builder = ChangeBuilder.getNewBuilder();
        builder.addListProperty(TestObject::getListOfSubObjects, TestSubObject::getId, a -> "", r -> "", subBuilder.getChangeGenerator());

        ChangeGenerator<TestObject, String> generator = builder.getChangeGenerator();

        List<TestSubObject> originalList = new ArrayList<>();
        originalList.add(new TestSubObject(1, "One"));
        originalList.add(new TestSubObject(2, "Two"));
        originalList.add(new TestSubObject(3, "Three"));

        List<TestSubObject> currentList = new ArrayList<>();
        currentList.add(new TestSubObject(1, "One"));
        currentList.add(new TestSubObject(2, "Dos"));
        currentList.add(new TestSubObject(3, "Three"));

        TestObject original = new TestObject(1, "Original");
        original.getListOfSubObjects().addAll(originalList);

        TestObject current = new TestObject(1, "Current");
        current.getListOfSubObjects().addAll(currentList);

        List<String> result = generator.getChanges(original, current);

        assertEquals(1, result.size());
    }

    class TestObject
    {
        private final int id;
        private final String name;
        private final List<Integer> listOfInts = new ArrayList<>();
        private final List<TestSubObject> listOfSubObjects = new ArrayList<>();

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

        public List<TestSubObject> getListOfSubObjects() {
            return listOfSubObjects;
        }
    }

    class TestSubObject
    {
        private final int id;
        private final String subName;

        public TestSubObject(int id, String subName) {
            this.id = id;
            this.subName = subName;
        }

        public int getId() {
            return id;
        }
    }
}
