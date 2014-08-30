package org.stevens.console;

import org.stevens.changegen.ChangeBuilder;
import org.stevens.changegen.ChangeGenerator;

import java.util.List;

public class Program {
    public static void main(final String[] args) {
        ChangeBuilder<Point, String> builder = ChangeBuilder.getNewBuilder();

        builder.addSelectorProperty(Point::getX, (original, current) -> String.format("X changed from %d to %d", original.getX(), current.getX()))
               .addSelectorProperty(Point::getY, (original, current) -> String.format("Y changed from %d to %d", original.getY(), current.getY()))
               .addSelectorProperty(Point::getZ, (original, current) -> String.format("Z changed from %d to %d", original.getZ(), current.getZ()));

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
