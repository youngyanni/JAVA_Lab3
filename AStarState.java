import java.util.HashMap;
/**
 *этот класс хранит набор открытых и закрытых
 * вершин, и предоставляет основные операции, необходимые для
 * функционирования алгоритма поиска А*.
 **/
public class AStarState
{
    /** Это ссылка на карту, по которой перемещается алгоритм A * **/
    private Map2D map;
    private HashMap <Location, Waypoint> openWaypoints=new HashMap<>();//Нестатическое поле для открытых вершин
    private HashMap <Location, Waypoint> closedWaypoints=new HashMap<>();//Нестатическое поле для закрытых вершин


    /**
     * Инициализировать новый объект состояния для использования алгоритма поиска пути A *.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");
        this.map = map;
    }
    /** Возвращает кол-во точек в наборе открытых вершин  **/
    public int numOpenWaypoints()
    {
        return openWaypoints.size();
    }//№1

    /** Возвращает карту, по которой перемещается поисковик A *. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     Функция, которая проверяет все вершины в наборе открытых вершин и после этого возвращает ссылку на вершину с
     наименьшей общей стоимостью.
     **/

    public Waypoint getMinOpenWaypoint()//№2
    {
        if (numOpenWaypoints()==0)
            return null;
        else {
            Waypoint minCostWaypoint = null;
            float minCost = Float.MAX_VALUE;
            for (Waypoint wp : openWaypoints.values()) /**Возвращает множество элементов коллекции **/
            {
                float totalCost = wp.getTotalCost();
                if (totalCost < minCost)
                {
                    minCostWaypoint = wp;
                    minCost = totalCost;
                }
            }
            return minCostWaypoint;
        }
    }

    /**
     * Этот метод добавляет путевую точку (или потенциально обновляет уже имеющуюся путевую точку)
     * в коллекцию «открытых путевых точек». Если в местоположении новой путевой точки еще нет
     * открытой путевой точки, то новая путевая точка просто добавляется в коллекцию.
     * Однако, если путевая точка уже существует в местоположении новой путевой точки,
     * новая путевая точка заменяет старую <em> только в том случае, если </em> значение
     * «предыдущей стоимости» новой путевой точки меньше, чем значение «предыдущей стоимости»
     * текущей путевой точки.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)//№3
    {
        Location loc = newWP.getLocation();
        if (!openWaypoints.containsKey(loc))/**Проверяет наличие ключа в коллекции**/
        {
            openWaypoints.put(loc, newWP);/**Добавляет в коллекцию пару (key, value)**/
            return true;
        }
        else {

            Waypoint oldWP = openWaypoints.get(loc);/**Возвращает значение по ключу.**/
            if (newWP.getPreviousCost() < oldWP.getPreviousCost()) {//Проверяет стоимость пути
                openWaypoints.put(loc, oldWP);
                return true;
            }
            return false;
        }
    }
    /**
     * Возвращает true, если набор закрытых путевых точек содержит путевую точку для указанного местоположения.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closedWaypoints.containsKey(loc);
    }//№4
    /**
     * Этот метод перемещает путевую точку в указанном месте из открытого списка в закрытый.
     **/
    public void closeWaypoint(Location loc)//№5
    {
        Waypoint wp = openWaypoints.remove(loc);//Удаляем вершину из набора открытых вершин
        closedWaypoints.put(loc, wp);//Добавляем её в закрытый набор
    }
}

