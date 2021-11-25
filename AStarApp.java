import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * A simple Swing application to demonstrate the A* pathfinding algorithm.  The
 * user is presented with a map, containing a start and end location.  The user
 * can draw or clear obstacles on the map, and then press a button to compute a
 * path from start to end using the A* pathfinding algorithm.  If a path is
 * found, it is displayed in green.
 **/
public class AStarApp {

    /** Количество ячеек сетки по оси X. **/
    private int width;
    
    /** Количество ячеек сетки по оси Y. **/
    private int height;
    
    /** Место, откуда начинается путь. **/
    private Location startLoc;
    
    /** Место, где путь должен закончиться. **/
    private Location finishLoc;
    
    /**
     * Это двухмерный массив компонентов пользовательского интерфейса,
     * которые обеспечивают отображение ячеек на карте и управление ими.
     ***/
    private JMapCell[][] mapCells;

    /**
     * Этот внутренний класс обрабатывает события мыши в основной сетке ячеек карты,
     * изменяя ячейки на основе состояния кнопки мыши и начального редактирования,
     * которое было выполнено.
     **/
    private class MapCellHandler implements MouseListener
    {
        /**
         * Это значение будет истинным, если была нажата кнопка мыши,
         * и в настоящее время мы находимся в процессе модификации.
         **/
        private boolean modifying;
        
        /**
         * Это значение фиксирует, делаем ли мы клетки проходимыми или непроходимыми.
         * Это зависит от исходного состояния ячейки, в которой была запущена операция.
         **/
        private boolean makePassable;
        
        /** Запускает операцию модификации. **/
        public void mousePressed(MouseEvent e)
        {
            modifying = true;
            
            JMapCell cell = (JMapCell) e.getSource();
            
            // Если текущая ячейка проходима, то делаем их
            // непроходимый; если непроходимо, то делаем их проходимыми.
            
            makePassable = !cell.isPassable();
            
            cell.setPassable(makePassable);
        }

        /** Завершает операцию модификации. **/
        public void mouseReleased(MouseEvent e)
        {
            modifying = false;
        }
        
        /**
         * Если была нажата мышь, это продолжит операцию модификации в новой ячейке.
         **/
        public void mouseEntered(MouseEvent e)
        {
            if (modifying)
            {
                JMapCell cell = (JMapCell) e.getSource();
                cell.setPassable(makePassable);
            }
        }

        /** Не требуется для этого обработчика **/
        public void mouseExited(MouseEvent e)
        {
            // Это мы игнорируем.
        }
        
        /** Не требуется для этого обработчика. **/
        public void mouseClicked(MouseEvent e)
        {
            // И этот тоже.
        }
    }
    
    
    /**
     * Создает новый экземпляр AStarApp с указанной шириной и высотой карты.
     **/
    public AStarApp(int w, int h) {
        if (w <= 0)
            throw new IllegalArgumentException("w must be > 0; got " + w);
        
        if (h <= 0)
            throw new IllegalArgumentException("h must be > 0; got " + h);
        
        width = w;
        height = h;
        
        startLoc = new Location(2, h / 2);
        finishLoc = new Location(w - 3, h / 2);
    }
    
    
    /**
     * Простой вспомогательный метод для настройки пользовательского интерфейса Swing.
     * Это вызывается из потока обработчика событий Swing для обеспечения безопасности потоков.
     **/
    private void initGUI()
    {
        JFrame frame = new JFrame("Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());

        // Используйте GridBagLayout, потому что он действительно учитывает предпочтительный размер
        // определяется компонентами, которые он выкладывает.
        
        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.insets.set(0, 0, 1, 1);

        JPanel mapPanel = new JPanel(gbLayout);
        mapPanel.setBackground(Color.GRAY);
        
        mapCells = new JMapCell[width][height];
        
        MapCellHandler cellHandler = new MapCellHandler();
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y] = new JMapCell();

                gbConstraints.gridx = x;
                gbConstraints.gridy = y;

                gbLayout.setConstraints(mapCells[x][y], gbConstraints);
                
                mapPanel.add(mapCells[x][y]);
                mapCells[x][y].addMouseListener(cellHandler);
            }
        }
        
        contentPane.add(mapPanel, BorderLayout.CENTER);
        
        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { findAndShowPath(); }
        });
        
        contentPane.add(findPathButton, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setVisible(true);

        mapCells[startLoc.xCoord][startLoc.yCoord].setEndpoint(true);
        mapCells[finishLoc.xCoord][finishLoc.yCoord].setEndpoint(true);
    }

    
    /** Запускает приложение. Вызывается из {@link #main} метод. **/
    private void start()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { initGUI(); }
        });
    }
    

    /**
     Этот вспомогательный метод пытается вычислить путь, используя текущее состояние карты.
     Реализация довольно медленная; создается новый объект {@link Map2D}, который инициализируется из текущего состояния приложения.
     Затем вызывается средство поиска пути A *, и, если путь найден, дисплей обновляется, чтобы показать найденный путь.
     (Лучшее решение - использовать шаблон проектирования Model View Controller.)
     **/
    private void findAndShowPath()
    {
        // Создайте объект Map2D, содержащий текущее состояние пользовательского ввода.

        Map2D map = new Map2D(width, height);
        map.setStart(startLoc);
        map.setFinish(finishLoc);
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y].setPath(false);

                if (mapCells[x][y].isPassable())
                    map.setCellValue(x, y, 0);
                else
                    map.setCellValue(x, y, Integer.MAX_VALUE);
            }
        }
        
        // Попробуйте вычислить путь. Если можно вычислить, отметьте все ячейки
        // на пути
        
        Waypoint wp = AStarPathfinder.computePath(map);
        
        while (wp != null)
        {
            Location loc = wp.getLocation();
            mapCells[loc.xCoord][loc.yCoord].setPath(true);
            
            wp = wp.getPrevious();
        }
    }
    
    
    /**
     * Точка входа в приложение.
     * В настоящее время аргументы командной строки не распознаются..
     **/
    public static void main(String[] args) {
        AStarApp app = new AStarApp(40, 30);
        app.start();
    }   
}
