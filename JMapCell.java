import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * Этот класс представляет собой настраиваемый компонент Swing для представления
 * отдельной ячейки карты на 2D-карте. Ячейка имеет несколько различных состояний,
 * но самое основное - является ли ячейка проходимой или нет.
 */
public class JMapCell extends JComponent
{
    private static final Dimension CELL_SIZE = new Dimension(12, 12);
    
    /** Истина указывает, что ячейка является конечной точкой, либо началом, либо концом.. **/
    boolean endpoint = false;
    
    
    /** Истина указывает, что ячейка проходима; false означает, что это не так. **/
    boolean passable = true;
    
    /**
     * Истина указывает, что эта ячейка является частью пути между началом и концом.
     **/
    boolean path = false;
    
    /**
     Постройте новую ячейку карты с указанной «проходимостью».Значение true означает, что ячейка проходима.
     **/
    public JMapCell(boolean pass)
    {
        // Set the preferred cell size, to drive the initial window size.
        setPreferredSize(CELL_SIZE);
        
        setPassable(pass);
    }
    
    /** Создайте новую ячейку карты, которая по умолчанию является проходимой. **/
    public JMapCell()
    {
        // Call the other constructor, specifying true for "passable".
        this(true);
    }
    
    /** Отмечает эту ячейку как начальную или конечную. **/
    public void setEndpoint(boolean end)
    {
        endpoint = end;
        updateAppearance();
    }
    
    /**
     * Делает эту ячейку проходимой или непроходимой. Значение true отмечает ячейку
     * как пригодную; ввод false помечает его как непроходимый.
     **/
    public void setPassable(boolean pass)
    {
        passable = pass;
        updateAppearance();
    }
    
    /** Возвращает true, если эта ячейка проходима, или false в противном случае. **/
    public boolean isPassable()
    {
        return passable;
    }
    
    /** Переключает текущее "проходимое" состояние ячейки карты.. **/
    public void togglePassable()
    {
        setPassable(!isPassable());
    }
    
    /** Помечает эту ячейку как часть пути, обнаруженного алгоритмом A *. **/
    public void setPath(boolean path)
    {
        this.path = path;
        updateAppearance();
    }
    
    /**
     * Этот вспомогательный метод обновляет цвет фона в соответствии
     * с текущим внутренним состоянием ячейки.
     **/
    private void updateAppearance()
    {
        if (passable)
        {
            // Passable cell.  Indicate its state with a border.
            setBackground(Color.WHITE);

            if (endpoint)
                setBackground(Color.CYAN);
            else if (path)
                setBackground(Color.GREEN);
        }
        else
        {
            // Impassable cell.  Make it all red.
            setBackground(Color.RED);
        }
    }

    /**
     * Реализация метода рисования для рисования цвета фона в ячейке карты
     **/
    protected void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
