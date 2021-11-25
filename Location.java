import java.util.Objects;
/**
 Этот класс представляет конкретное место на 2D-карте. Координаты - это целые числа.
 **/
public class Location
{
    /** Координата X этого места. **/
    public int xCoord;
    /** Координата Y этого места. **/
    public int yCoord;
    /** Создает новое местоположение с указанными целыми координатами. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }
    /** Создает новое местоположение с координатами (0, 0). **/
    public Location()
    {
        this(0, 0);
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;// Сравниваем объект о с текущим объектом(ссылки)
        if (o == null || getClass() != o.getClass()) return false;//Сравнение классов
        Location location = (Location) o;
        return xCoord == location.xCoord && yCoord == location.yCoord;//Возвращаем значение сравнения полей классов
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }
}
