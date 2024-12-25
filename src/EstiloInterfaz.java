import javax.swing.*;
import java.awt.*;

/**
 * La clase EstiloInterfaz proporciona métodos estáticos para aplicar estilos visuales a los componentes de interfaz gráfica.
 */
public class EstiloInterfaz {

    /**
     * Aplica un estilo visual a un componente de interfaz gráfica.
     *
     * @param componente El componente al que se aplicará el estilo.
     */
    public static void aplicarEstilo(Component componente) {
        if (componente instanceof JButton) {
            JButton boton = (JButton) componente;
            boton.setBackground(new Color(91, 91, 91));
            boton.setForeground(Color.WHITE);
            boton.setFocusPainted(false);
            boton.setBorder(BorderFactory.createRaisedBevelBorder());
            Font buttonFont = new Font("Arial", Font.BOLD, 16);
            boton.setFont(buttonFont);
        } else if (componente instanceof JPanel) {

            JPanel panel = (JPanel) componente;
            panel.setBackground(new Color(220, 220, 220));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
    }
}
