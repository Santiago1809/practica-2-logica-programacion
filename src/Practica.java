import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica extends JFrame {
    private int[] arr; // Usamos arr como el único array
    private JPanel panel;
    private JButton sortButton, searchButton, updateButton, deleteButton;
    private JTextField searchField, updateField, deleteField;
    private final int delay = 200; // Tiempo de espera entre pasos

    public Practica() {
        // Inicializar el array
        arr = new int[50];
        Arreglo.llenarVector(50, arr);

        // Configurar la ventana
        setTitle("Visualizador de Ordenamiento y Búsqueda");
        setSize(1200, 800); // Aumentar el tamaño para más espacio
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawArray(g);
            }
        };

        sortButton = new JButton("Ordenar");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortButton.setEnabled(false); // Deshabilitar el botón
                new Thread(() -> {
                    selectionSort(arr);
                    sortButton.setEnabled(true); // Habilitar después de ordenar
                    JOptionPane.showMessageDialog(null, "Array ordenado");
                }).start();
            }
        });

        searchField = new JTextField(10);
        searchButton = new JButton("Buscar");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int target;
                try {
                    target = Integer.parseInt(searchField.getText());
                    new Thread(() -> {
                        binarySearch(arr, target);
                    }).start();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Practica.this, "Por favor ingrese un número válido.");
                }
            }
        });

        // Nuevo campo y botón para actualizar
        updateField = new JTextField(10);
        updateButton = new JButton("Actualizar");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = updateField.getText().split(",");
                if (input.length == 2) {
                    try {
                        int index = Integer.parseInt(input[0].trim());
                        int newValue = Integer.parseInt(input[1].trim());
                        if (valueExists(newValue)) {
                            JOptionPane.showMessageDialog(Practica.this, "El valor ya existe en el array.");
                        } else {
                            updateValue(index, newValue);
                            sortButton.doClick(); // Ordenar el array después de la actualización
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(Practica.this, "Entrada no válida. Formato: index,nuevoValor.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Practica.this, "Por favor ingrese los datos en el formato: index,nuevoValor.");
                }
            }
        });

        // Nuevo campo y botón para eliminar
        deleteField = new JTextField(10);
        deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int valueToDelete;
                try {
                    valueToDelete = Integer.parseInt(deleteField.getText());
                    deleteValue(valueToDelete);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Practica.this, "Por favor ingrese un número válido.");
                }
            }
        });

        // Usar un panel para botones y campos de búsqueda, actualización y eliminación
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(updateField);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteField);
        buttonPanel.add(deleteButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void drawArray(Graphics g) {
        panel.setBackground(Color.WHITE);
        int width = panel.getWidth() / arr.length; // Ancho de cada casilla
        int height = panel.getHeight() / 2; // Altura de las casillas
        for (int i = 0; i < arr.length; i++) {
            g.setColor(Color.BLACK);
            g.drawRect(i * width, panel.getHeight() / 4, width - 2, height); // Casilla
            g.drawString(String.valueOf(arr[i]), i * width + width / 4, panel.getHeight() / 4 + height / 2); // Valor
        }
    }

    private void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // Intercambiar el elemento mínimo encontrado con el primer elemento
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;

            // Visualizar el cambio
            panel.repaint();
            drawArray(panel.getGraphics());
            sleep(150); // Tiempo de espera entre intercambios
        }
    }

    private void binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            // Visualizar la búsqueda
            drawArrayHighlight(panel.getGraphics(), mid, arr[mid] == target);
            sleep(delay);

            // Actualizar el panel
            panel.repaint();
            drawArray(panel.getGraphics());

            if (arr[mid] == target) {
                JOptionPane.showMessageDialog(this, "Elemento encontrado en la posición: " + mid);
                return;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        JOptionPane.showMessageDialog(this, "Elemento no encontrado.");
    }

    private void drawArrayHighlight(Graphics g, int index, boolean found) {
        drawArray(g);
        int width = panel.getWidth() / arr.length;

        // Resaltar el elemento actual
        g.setColor(found ? Color.GREEN : Color.RED);
        g.fillRect(index * width, panel.getHeight() / 4, width - 2, panel.getHeight() / 2); // Altura ajustada
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(arr[index]), index * width + width / 4, panel.getHeight() / 4 + panel.getHeight() / 4 + (panel.getHeight() / 2) / 2); // Centrado
    }

    private void updateValue(int index, int newValue) {
        if (index >= 0 && index < arr.length) {
            arr[index] = newValue;
            panel.repaint(); // Actualizar la visualización
            drawArray(panel.getGraphics());
            JOptionPane.showMessageDialog(this, "Elemento actualizado en la posición: " + index);
        } else {
            JOptionPane.showMessageDialog(this, "Índice fuera de rango.");
        }
    }

    private boolean valueExists(int value) {
        return Arreglo.buscarSecuencial(value, arr) != -1;
    }

    private void deleteValue(int value) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            int[] newArr = new int[arr.length - 1];
            for (int i = 0, j = 0; i < arr.length; i++) {
                if (i != index) {
                    newArr[j++] = arr[i];
                }
            }
            arr = newArr;
            panel.repaint(); // Actualizar la visualización
            drawArray(panel.getGraphics());
            JOptionPane.showMessageDialog(this, "Elemento " + value + " eliminado.");
        } else {
            JOptionPane.showMessageDialog(this, "Elemento no encontrado en el array.");
        }
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
