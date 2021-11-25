/**
 * Этот класс представляет собой простую двухмерную карту, состоящую из квадратных ячеек.
 * Каждая ячейка определяет стоимость прохождения этой ячейки.
 **/
public class Map2D
{
    /** Ширина карты. **/
    private int width;

    /** Высота карты. **/
    private int height;

    /**
     * Фактические данные карты, которые необходимы алгоритму поиска пути.
     **/
    private int[][] cells;

    /** Начальная точка для поиска пути A *. **/
    private Location start;

    /** Конечное место для поиска пути A *. **/
    private Location finish;


    /** Создает новую 2D-карту с указанной шириной и высотой.. **/
    public Map2D(int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException(
                    "width and height must be positive values; got " + width +
                    "x" + height);
        }
        
        this.width = width;
        this.height = height;
        
        cells = new int[width][height];
        
        // Составьте некоторые координаты начала и конца.
        start = new Location(0, height / 2);
        finish = new Location(width - 1, height / 2);
    }


    /**
     * Этот вспомогательный метод проверяет указанные координаты, чтобы убедиться,
     * что они находятся в границах карты. Если координаты не находятся в пределах карты,
     * метод генерирует исключение <code> IllegalArgumentException </code>.
     **/
    private void checkCoords(int x, int y)
    {
        if (x < 0 || x > width)
        {
            throw new IllegalArgumentException("x must be in range [0, " + 
                    width + "), got " + x);
        }
        
        if (y < 0 || y > height)
        {
            throw new IllegalArgumentException("y must be in range [0, " + 
                    height + "), got " + y);
        }
    }    
    
    /** Возвращает ширину карты. **/
    public int getWidth()
    {
        return width;
    }
    
    /** Возвращает высоту карты.. **/
    public int getHeight()
    {
        return height;
    }
    
    /**
     * Возвращает истину, если указанные координаты находятся в пределах области карты.
     **/
    public boolean contains(int x, int y)
    {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    
    /** Возвращает истину, если местоположение содержится в области карты. **/
    public boolean contains(Location loc)
    {
        return contains(loc.xCoord, loc.yCoord);
    }
    
    /** Возвращает сохраненное значение стоимости для указанной ячейки. **/
    public int getCellValue(int x, int y)
    {
        checkCoords(x, y);
        return cells[x][y];
    }
    
    /** Возвращает сохраненное значение стоимости для указанной ячейки. **/
    public int getCellValue(Location loc)
    {
        return getCellValue(loc.xCoord, loc.yCoord);
    }
    
    /** Устанавливает значение стоимости для указанной ячейки. **/
    public void setCellValue(int x, int y, int value)
    {
        checkCoords(x, y);
        cells[x][y] = value;
    }
    
    /**
     * Возвращает начальное местоположение карты. Здесь будет начинаться сгенерированный путь
     **/
    public Location getStart() { return start; }
    
    /**
     * Устанавливает начальную точку для карты. Здесь будет начинаться сгенерированный путь
     **/
    public void setStart(Location loc)
    {
        if (loc == null)
            throw new NullPointerException("loc cannot be null");
        
        start = loc;
    }

    /**
     * Возвращает конечное местоположение карты. Здесь сгенерированный путь завершится
     **/
    public Location getFinish()
    {
        return finish;
    }
    
    /**
     * Устанавливает конечное местоположение для карты. Здесь сгенерированный путь завершится
     **/
    public void setFinish(Location loc)
    {
        if (loc == null)
            throw new NullPointerException("loc cannot be null");
        
        finish = loc;
    }
}
