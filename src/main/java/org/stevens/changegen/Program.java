package org.stevens.changegen;

import java.util.List;

public class Program {
    public static void main(final String[] args) {
        ChangeBuilder<Point, String> builder = new ChangeBuilderImpl<>();

        builder.AddSelectorProperty(Point::getX, (original, current) -> String.format("X changed from %d to %d", original.getX(), current.getX()))
               .AddSelectorProperty(Point::getY, (original, current) -> String.format("Y changed from %d to %d", original.getY(), current.getY()))
               .AddSelectorProperty(Point::getZ, (original, current) -> String.format("Z changed from %d to %d", original.getZ(), current.getZ()));

        ChangeGenerator<Point, String> generator = builder.getChangeGenerator();

        Point point1 = new Point(1,2,3);
        Point point2 = new Point(2,3,4);

        List<String> changes = generator.getChanges(point1, point2);

        for(String change : changes)
        {
            System.out.println(change);
        }
    }
}
