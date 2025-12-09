package com.kanoma;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Visualisation animée du sapin de Noël (Day 07) avec export GIF
 * - Fond noir
 * - Caractères de la grille en blanc
 * - Traits colorés (rouge/vert) lors des splits
 */
public class Day07Visualizer extends JPanel {

    private static final int CELL_SIZE = 8;
    private static final int ANIMATION_DELAY = 100; // ms entre chaque ligne
    
    private final List<char[]> grid;
    private final List<Set<Integer>> pathHistory;
    private final List<Map<Integer, Color>> splitColors;
    private final List<Integer> cumulativeScore; // Score cumulatif par ligne
    private int totalScore = 0;
    
    private int currentLine = 0;
    private javax.swing.Timer animationTimer;
    private boolean animationComplete = false;
    private Random random = new Random(42); // Seed fixe pour reproductibilité
    
    // Couleurs de Noël
    private static final Color CHRISTMAS_RED = new Color(200, 30, 30);
    private static final Color CHRISTMAS_GREEN = new Color(34, 139, 34);
    private static final Color STAR_GOLD = new Color(255, 215, 0);
    private static final Color GRID_WHITE = new Color(200, 200, 200);
    private static final Color PATH_WHITE = Color.WHITE;
    
    private final int panelWidth;
    private final int panelHeight;
    
    public Day07Visualizer(List<char[]> grid) {
        this.grid = grid;
        this.pathHistory = new ArrayList<>();
        this.splitColors = new ArrayList<>();
        
        for (int i = 0; i < grid.size(); i++) {
            pathHistory.add(new HashSet<>());
            splitColors.add(new HashMap<>());
        }
        this.cumulativeScore = new ArrayList<>();
        
        int maxWidth = 0;
        for (char[] line : grid) {
            maxWidth = Math.max(maxWidth, line.length);
        }
        panelWidth = maxWidth * CELL_SIZE + 40;
        panelHeight = grid.size() * CELL_SIZE + 60;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);
        
        calculatePaths();
    }
    
    private void calculatePaths() {
        Set<Integer> currentPositions = new HashSet<>();
        int runningScore = 0;
        
        char[] firstLine = grid.get(0);
        for (int i = 0; i < firstLine.length; i++) {
            if (firstLine[i] == 'S') {
                currentPositions.add(i);
            }
        }
        pathHistory.get(0).addAll(currentPositions);
        cumulativeScore.add(runningScore); // Score à la ligne 0
        
        for (int lineIdx = 1; lineIdx < grid.size(); lineIdx++) {
            char[] line = grid.get(lineIdx);
            Set<Integer> newPositions = new HashSet<>();
            
            for (Integer pos : currentPositions) {
                if (pos >= 0 && pos < line.length) {
                    char c = line[pos];
                    if (c == '.') {
                        newPositions.add(pos);
                    } else if (c == '^') {
                        runningScore++; // Incrémenter le score sur un split
                        Color splitColor = random.nextBoolean() ? CHRISTMAS_RED : CHRISTMAS_GREEN;
                        splitColors.get(lineIdx).put(pos, splitColor);
                        
                        if (pos - 1 >= 0) newPositions.add(pos - 1);
                        if (pos + 1 < line.length) newPositions.add(pos + 1);
                    }
                }
            }
            
            cumulativeScore.add(runningScore); // Enregistrer le score cumulatif
            pathHistory.get(lineIdx).addAll(newPositions);
            currentPositions = newPositions;
        }
        
        totalScore = runningScore;
    }
    
    public void setCurrentLine(int line) {
        this.currentLine = line;
        this.animationComplete = (line >= grid.size() - 1);
    }
    
    public void startAnimation() {
        animationTimer = new javax.swing.Timer(ANIMATION_DELAY, e -> {
            if (currentLine < grid.size() - 1) {
                currentLine++;
                repaint();
            } else {
                animationTimer.stop();
                animationComplete = true;
                repaint();
            }
        });
        animationTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderToGraphics((Graphics2D) g);
    }
    
    private void renderToGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int offsetX = 20;
        int offsetY = 20;
        
        g2d.setFont(new Font("Monospaced", Font.BOLD, CELL_SIZE));
        
        for (int lineIdx = 0; lineIdx < grid.size(); lineIdx++) {
            char[] line = grid.get(lineIdx);
            boolean showPath = lineIdx <= currentLine;
            
            for (int col = 0; col < line.length; col++) {
                char c = line[col];
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + lineIdx * CELL_SIZE + CELL_SIZE;
                
                Color charColor = GRID_WHITE;
                
                if (c == 'S') {
                    charColor = STAR_GOLD;
                    g2d.setColor(charColor);
                    g2d.drawString("*", x, y); // Utiliser * pour compatibilité GIF
                    continue;
                }
                
                if (showPath && pathHistory.get(lineIdx).contains(col)) {
                    if (splitColors.get(lineIdx).containsKey(col)) {
                        charColor = splitColors.get(lineIdx).get(col);
                    } else {
                        charColor = PATH_WHITE;
                    }
                }
                
                g2d.setColor(charColor);
                g2d.drawString(String.valueOf(c), x, y);
            }
        }
        
        // Dessiner les traits de split
        g2d.setStroke(new BasicStroke(2));
        
        for (int lineIdx = 0; lineIdx < Math.min(currentLine, grid.size() - 1); lineIdx++) {
            Set<Integer> currentPos = pathHistory.get(lineIdx);
            Set<Integer> nextPos = pathHistory.get(lineIdx + 1);
            char[] nextLine = grid.get(lineIdx + 1);
            
            for (Integer pos : currentPos) {
                if (pos >= 0 && pos < nextLine.length && nextLine[pos] == '^') {
                    Color splitColor = splitColors.get(lineIdx + 1).getOrDefault(pos, CHRISTMAS_GREEN);
                    
                    int startX = offsetX + pos * CELL_SIZE + CELL_SIZE / 2;
                    int startY = offsetY + lineIdx * CELL_SIZE + CELL_SIZE;
                    
                    if (pos - 1 >= 0 && nextPos.contains(pos - 1)) {
                        int endX = offsetX + (pos - 1) * CELL_SIZE + CELL_SIZE / 2;
                        int endY = offsetY + (lineIdx + 2) * CELL_SIZE;
                        g2d.setColor(splitColor);
                        g2d.drawLine(startX, startY, endX, endY);
                    }
                    
                    if (pos + 1 < nextLine.length && nextPos.contains(pos + 1)) {
                        int endX = offsetX + (pos + 1) * CELL_SIZE + CELL_SIZE / 2;
                        int endY = offsetY + (lineIdx + 2) * CELL_SIZE;
                        Color altColor = splitColor == CHRISTMAS_RED ? CHRISTMAS_GREEN : CHRISTMAS_RED;
                        g2d.setColor(altColor);
                        g2d.drawLine(startX, startY, endX, endY);
                    }
                }
            }
        }
        
        // Score en haut à droite avec effet doré brillant
        int currentScore = currentLine < cumulativeScore.size() ? cumulativeScore.get(currentLine) : totalScore;
        String scoreText = "Score: " + currentScore;
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int scoreX = panelWidth - fm.stringWidth(scoreText) - 20;
        int scoreY = 25;
        
        // Effet brillant : ombre légère
        g2d.setColor(new Color(180, 140, 0)); // Or foncé pour l'ombre
        g2d.drawString(scoreText, scoreX + 1, scoreY + 1);
        
        // Texte doré brillant avec dégradé
        GradientPaint goldGradient = new GradientPaint(
            scoreX, scoreY - 10, new Color(255, 223, 0),   // Or clair
            scoreX, scoreY + 5, new Color(255, 180, 0)     // Or foncé
        );
        g2d.setPaint(goldGradient);
        g2d.drawString(scoreText, scoreX, scoreY);
        
        // Titre en bas
        g2d.setColor(STAR_GOLD);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("Advent of Code 2025 - Day 07 - Christmas Tree", offsetX, panelHeight - 25);
        
        if (animationComplete) {
            g2d.setColor(CHRISTMAS_GREEN);
            g2d.drawString("Animation terminee! Score final: " + totalScore, offsetX, panelHeight - 10);
        } else {
            g2d.setColor(GRID_WHITE);
            g2d.drawString("Ligne: " + currentLine + "/" + (grid.size() - 1), offsetX, panelHeight - 10);
        }
    }
    
    /**
     * Génère une frame de l'animation à la ligne spécifiée
     */
    public BufferedImage generateFrame(int lineNumber) {
        BufferedImage image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond noir
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, panelWidth, panelHeight);
        
        // Définir la ligne courante et dessiner
        int savedLine = currentLine;
        boolean savedComplete = animationComplete;
        
        currentLine = lineNumber;
        animationComplete = (lineNumber >= grid.size() - 1);
        
        renderToGraphics(g2d);
        
        currentLine = savedLine;
        animationComplete = savedComplete;
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Exporte l'animation en GIF
     */
    public void exportToGif(String filename, int frameDelayMs) throws IOException {
        System.out.println("Export GIF vers: " + filename);
        System.out.println("Nombre de frames: " + grid.size());
        
        ImageOutputStream output = new FileImageOutputStream(new File(filename));
        GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, frameDelayMs, true);
        
        for (int i = 0; i < grid.size(); i++) {
            BufferedImage frame = generateFrame(i);
            writer.writeToSequence(frame);
            
            if (i % 10 == 0) {
                System.out.println("Frame " + i + "/" + (grid.size() - 1));
            }
        }
        
        // Ajouter une pause plus longue à la fin pour bien voir le score final
        System.out.println("Ajout des frames de pause finale (score: " + totalScore + ")");
        BufferedImage lastFrame = generateFrame(grid.size() - 1);
        for (int i = 0; i < 30; i++) { // 30 frames = 3 secondes de pause
            writer.writeToSequence(lastFrame);
        }
        
        writer.close();
        output.close();
        
        System.out.println("GIF exporté avec succès: " + filename);
    }
    
    /**
     * Classe utilitaire pour écrire des GIF animés
     */
    static class GifSequenceWriter {
        protected ImageWriter gifWriter;
        protected ImageWriteParam imageWriteParam;
        protected IIOMetadata imageMetaData;

        public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
            gifWriter = ImageIO.getImageWritersBySuffix("gif").next();
            imageWriteParam = gifWriter.getDefaultWriteParam();
            ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);

            imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

            String metaFormatName = imageMetaData.getNativeMetadataFormatName();

            IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

            IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
            graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
            graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
            graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));
            graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

            IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
            commentsNode.setAttribute("CommentExtension", "Created by Day07Visualizer");

            IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
            IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
            child.setAttribute("applicationID", "NETSCAPE");
            child.setAttribute("authenticationCode", "2.0");

            int loop = loopContinuously ? 0 : 1;
            child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
            appExtensionsNode.appendChild(child);

            imageMetaData.setFromTree(metaFormatName, root);

            gifWriter.setOutput(outputStream);
            gifWriter.prepareWriteSequence(null);
        }

        public void writeToSequence(RenderedImage img) throws IOException {
            gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
        }

        public void close() throws IOException {
            gifWriter.endWriteSequence();
        }

        private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
            int nNodes = rootNode.getLength();
            for (int i = 0; i < nNodes; i++) {
                if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                    return ((IIOMetadataNode) rootNode.item(i));
                }
            }
            IIOMetadataNode node = new IIOMetadataNode(nodeName);
            rootNode.appendChild(node);
            return node;
        }
    }
    
    public static void main(String[] args) {
        String day = "07";
        boolean exportGif = false;
        String gifFilename = "christmas_tree.gif";
        
        // Parser les arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--gif") || args[i].equals("-g")) {
                exportGif = true;
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    gifFilename = args[++i];
                }
            } else if (args[i].equals("--day") || args[i].equals("-d")) {
                if (i + 1 < args.length) {
                    day = args[++i];
                }
            } else if (!args[i].startsWith("-")) {
                day = args[i];
            }
        }
        
        final String finalDay = day;
        final boolean finalExportGif = exportGif;
        final String finalGifFilename = gifFilename;
        
        try {
            Scanner scanner = Utils.getScannerForDay(finalDay);
            List<char[]> grid = new ArrayList<>();
            
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                grid.add(line.toCharArray());
            }
            scanner.close();
            
            Day07Visualizer visualizer = new Day07Visualizer(grid);
            
            if (finalExportGif) {
                // Mode export GIF (sans interface graphique)
                try {
                    visualizer.exportToGif(finalGifFilename, ANIMATION_DELAY);
                    System.out.println("GIF créé: " + finalGifFilename);
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'export GIF: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                // Mode interactif avec fenêtre
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Day 07 - Christmas Tree Visualizer");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    // Ajouter un bouton pour exporter
                    JPanel mainPanel = new JPanel(new BorderLayout());
                    JScrollPane scrollPane = new JScrollPane(visualizer);
                    scrollPane.getViewport().setBackground(Color.BLACK);
                    mainPanel.add(scrollPane, BorderLayout.CENTER);
                    
                    JButton exportButton = new JButton("Exporter en GIF");
                    exportButton.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setSelectedFile(new File("christmas_tree.gif"));
                        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            String path = file.getAbsolutePath();
                            if (!path.endsWith(".gif")) {
                                path += ".gif";
                            }
                            try {
                                visualizer.exportToGif(path, ANIMATION_DELAY);
                                JOptionPane.showMessageDialog(frame, "GIF exporté avec succès:\n" + path);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    mainPanel.add(exportButton, BorderLayout.SOUTH);
                    
                    frame.add(mainPanel);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    
                    javax.swing.Timer startTimer = new javax.swing.Timer(500, e -> visualizer.startAnimation());
                    startTimer.setRepeats(false);
                    startTimer.start();
                });
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Fichier non trouvé pour le jour: " + finalDay);
        }
    }
}
