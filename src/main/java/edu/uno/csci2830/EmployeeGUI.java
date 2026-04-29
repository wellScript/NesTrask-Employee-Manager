package edu.uno.csci2830;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Graphical User Interface for the NesTrack Employee Database.
 *
 * <p>Replaces the CLI in EmployeeMain with a Swing-based GUI.
 * All business logic still goes through EmployeeManager.</p>
 *
 * @author Shelby Wells
 * @version 1.0
 */
public class EmployeeGUI extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(18,  20,  28);
    private static final Color BG_PANEL     = new Color(28,  31,  43);
    private static final Color BG_ROW_ALT   = new Color(33,  37,  52);
    private static final Color ACCENT       = new Color(99, 179, 237);   // steel-blue
    private static final Color ACCENT_HOVER = new Color(144, 205, 244);
    private static final Color DANGER       = new Color(252, 129, 129);
    private static final Color SUCCESS      = new Color(104, 211, 145);
    private static final Color TEXT_PRIMARY = new Color(226, 232, 240);
    private static final Color TEXT_MUTED   = new Color(113, 128, 150);
    private static final Color BORDER_CLR   = new Color(45,  50,  70);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  22);
    private static final Font FONT_LABEL  = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("SansSerif", Font.BOLD,  12);
    private static final Font FONT_TABLE  = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD,  12);

    // ── Core ──────────────────────────────────────────────────────────────────
    private final EmployeeManager manager;
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JLabel statusBar;

    // ── Table columns ─────────────────────────────────────────────────────────
    private static final String[] COLUMNS =
        {"ID", "First Name", "Last Name", "Department", "Title", "Start Date", "Term Date", "Active"};

    // ─────────────────────────────────────────────────────────────────────────
    public EmployeeGUI() {
        manager = new EmployeeManager();
        manager.loadFromCSV();

        setTitle("NesTrack Employee Database");
        setSize(1050, 680);
        setMinimumSize(new Dimension(820, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        buildUI();
        refreshTable();
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  UI CONSTRUCTION
    // ─────────────────────────────────────────────────────────────────────────
    private void buildUI() {
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildToolbar(), BorderLayout.SOUTH);
    }

    /** 
     * Dark top bar with app title. 
     * */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("  NesTrack  ·  Employee Database");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);

        JLabel sub = new JLabel("v2.0   ");
        sub.setFont(FONT_LABEL);
        sub.setForeground(TEXT_MUTED);

        header.add(title, BorderLayout.WEST);
        header.add(sub,   BorderLayout.EAST);
        return header;
    }

    /** 
     * Table in the center + left sidebar with action buttons. 
     */
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BG_DARK);

        center.add(buildSidebar(), BorderLayout.WEST);
        center.add(buildTablePanel(), BorderLayout.CENTER);
        return center;
    }

    /** 
     * Left sidebar with action buttons. 
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_CLR));
        sidebar.setPreferredSize(new Dimension(170, 0));

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(sideLabel("EMPLOYEES"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(sideBtn("＋  Add Employee",   ACCENT,  e -> showAddDialog()));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(sideBtn("⊘  Terminate",       DANGER,  e -> showTerminateDialog()));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(sideBtn("＋ Activate",       DANGER,  e -> showActivateDialog()));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(sideLabel("EDIT"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(sideBtn("✎  Edit Selected",   ACCENT,  e -> showEditDialog()));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(sideLabel("SEARCH"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(sideBtn("⌕  By Name",          ACCENT,  e -> showSearchByNameDialog()));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(sideBtn("⌕  By ID",            ACCENT,  e -> showSearchByIdDialog()));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sideBtn("↺  Refresh",          TEXT_MUTED, e -> refreshTable()));
        sidebar.add(Box.createVerticalStrut(16));
        return sidebar;
    }

    /** 
     * Scrollable employee table. 
     */
    private JPanel buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        employeeTable = new JTable(tableModel);
        styleTable(employeeTable);

        JScrollPane scroll = new JScrollPane(employeeTable);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // Status bar inside table panel
        statusBar = new JLabel("  Ready");
        statusBar.setFont(FONT_LABEL);
        statusBar.setForeground(TEXT_MUTED);
        statusBar.setPreferredSize(new Dimension(0, 24));
        statusBar.setBackground(BG_PANEL);
        statusBar.setOpaque(true);
        statusBar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_DARK);
        tablePanel.add(scroll,    BorderLayout.CENTER);
        tablePanel.add(statusBar, BorderLayout.SOUTH);
        return tablePanel;
    }

    /** 
     * Bottom toolbar (just search bar for quick filtering). 
     */
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bar.setBackground(BG_PANEL);
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));

        JLabel lbl = new JLabel("Quick filter:");
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MUTED);

        JTextField filterField = new JTextField(24);
        styleTextField(filterField);
        filterField.putClientProperty("JTextField.placeholderText", "Type to filter by any column…");

        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(filterField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(filterField.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(filterField.getText()); }
        });

        bar.add(lbl);
        bar.add(filterField);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  DIALOGS
    // ─────────────────────────────────────────────────────────────────────────

    /** Dialog: Add a new employee. */
    private void showAddDialog() {
        JDialog dlg = createDialog("Add New Employee", 420, 380);

        JTextField fnField   = styledField();
        JTextField lnField   = styledField();
        JTextField deptField = styledField();
        JTextField titleField= styledField();
        JTextField dateField = styledField();
        dateField.setText(LocalDate.now().toString());

        JPanel form = formPanel(
            "First Name",  fnField,
            "Last Name",   lnField,
            "Department",  deptField,
            "Title",       titleField,
            "Start Date (YYYY-MM-DD)", dateField
        );

        JButton submit = accentButton("Add Employee", ACCENT);
        submit.addActionListener(e -> {
            if (anyBlank(fnField, lnField, deptField, titleField, dateField)) {
                setStatus("⚠  All fields are required.", DANGER);
                return;
            }
            try {
                LocalDate start = LocalDate.parse(dateField.getText().trim());
                Employee emp = manager.addEmployee(
                    fnField.getText().trim(),
                    lnField.getText().trim(),
                    deptField.getText().trim(),
                    titleField.getText().trim(),
                    start
                );
                manager.saveToCSV();
                refreshTable();
                dlg.dispose();
                setStatus("✔  Employee " + emp.getFirstName() + " " + emp.getLastName() + " added (ID " + emp.getId() + ").", SUCCESS);
            } catch (DateTimeParseException ex) {
                setStatus("⚠  Invalid date format. Use YYYY-MM-DD.", DANGER);
            }
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(submit), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /** Dialog: Terminate employee by ID or selected row. */
    private void showTerminateDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        String prefill = selectedRow >= 0
            ? tableModel.getValueAt(selectedRow, 0).toString()
            : "";

        JDialog dlg = createDialog("Terminate Employee", 360, 200);
        JTextField idField = styledField();
        idField.setText(prefill);

        JPanel form = formPanel("Employee ID", idField);

        JButton btn = accentButton("Terminate", DANGER);
        btn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Employee emp = manager.findEmployeeById(id);
                if (emp == null) {
                    setStatus("⚠  No employee found with ID " + id + ".", DANGER);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(dlg,
                    "Terminate " + emp.getFirstName() + " " + emp.getLastName() + "?",
                    "Confirm Termination", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    emp.disable(id);
                    manager.saveToCSV();
                    refreshTable();
                    dlg.dispose();
                    setStatus("⊘  Employee ID " + id + " terminated.", DANGER);
                }
            } catch (NumberFormatException ex) {
                setStatus("⚠  Please enter a valid numeric ID.", DANGER);
            }
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(btn), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /** Dialog: Terminate employee by ID or selected row. */
    private void showActivateDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        String prefill = selectedRow >= 0
            ? tableModel.getValueAt(selectedRow, 0).toString()
            : "";

        JDialog dlg = createDialog("Activate Employee", 360, 200);
        JTextField idField = styledField();
        idField.setText(prefill);

        JPanel form = formPanel("Employee ID", idField);

        JButton btn = accentButton("Activate", DANGER);
        btn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Employee emp = manager.findEmployeeById(id);
                if (emp == null) {
                    setStatus("⚠  No employee found with ID " + id + ".", DANGER);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(dlg,
                    "Activate " + emp.getFirstName() + " " + emp.getLastName() + "?",
                    "Confirm Activation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    emp.setActive(id);
                    manager.saveToCSV();
                    refreshTable();
                    dlg.dispose();
                    setStatus("⊘  Employee ID " + id + " Activated.", DANGER);
                }
            } catch (NumberFormatException ex) {
                setStatus("⚠  Please enter a valid numeric ID.", DANGER);
            }
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(btn), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /** Dialog: Edit selected employee's fields. */
    private void showEditDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            setStatus("⚠  Select an employee in the table first.", DANGER);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Employee emp = manager.findEmployeeById(id);
        if (emp == null) { setStatus("⚠  Employee not found.", DANGER); return; }

        JDialog dlg = createDialog("Edit Employee — ID " + id, 420, 340);

        JTextField fnField    = styledField(); fnField.setText(emp.getFirstName());
        JTextField lnField    = styledField(); lnField.setText(emp.getLastName());
        JTextField deptField  = styledField(); deptField.setText(emp.getDepartment());
        JTextField titleField = styledField(); titleField.setText(emp.getTitle());
        JTextField termDateField = styledField(); termDateField.setText(emp.getTermDate());

        JPanel form = formPanel(
            "First Name",  fnField,
            "Last Name",   lnField,
            "Department",  deptField,
            "Title",       titleField
        );

        JButton save = accentButton("Save Changes", ACCENT);
        save.addActionListener(e -> {
            emp.setFirstName(fnField.getText().trim());
            emp.setLastName(lnField.getText().trim());
            emp.setDepartment(deptField.getText().trim());
            emp.setTitle(titleField.getText().trim());
            emp.setTermDate(LocalDate.parse(termDateField.getText().trim()));
            manager.saveToCSV();
            refreshTable();
            dlg.dispose();
            setStatus("✔  Employee ID " + id + " updated.", SUCCESS);
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(save), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /** Dialog: Search by name. */
    private void showSearchByNameDialog() {
        JDialog dlg = createDialog("Search by Name", 360, 220);
        JTextField fnField = styledField();
        JTextField lnField = styledField();
        JPanel form = formPanel("First Name", fnField, "Last Name", lnField);

        JButton btn = accentButton("Search", ACCENT);
        btn.addActionListener(e -> {
            Employee emp = manager.findEmployeeByName(
                fnField.getText().trim(), lnField.getText().trim());
            dlg.dispose();
            if (emp != null) {
                highlightRow(emp.getId());
                setStatus("✔  Found: " + emp.getFirstName() + " " + emp.getLastName() + " (ID " + emp.getId() + ").", SUCCESS);
            } else {
                setStatus("⚠  No employee found with that name.", DANGER);
            }
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(btn), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /** Dialog: Search by ID. */
    private void showSearchByIdDialog() {
        JDialog dlg = createDialog("Search by ID", 340, 180);
        JTextField idField = styledField();
        JPanel form = formPanel("Employee ID", idField);

        JButton btn = accentButton("Search", ACCENT);
        btn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Employee emp = manager.findEmployeeById(id);
                dlg.dispose();
                if (emp != null) {
                    highlightRow(id);
                    setStatus("✔  Found: " + emp.getFirstName() + " " + emp.getLastName() + " (ID " + id + ").", SUCCESS);
                } else {
                    setStatus("⚠  No employee found with ID " + id + ".", DANGER);
                }
            } catch (NumberFormatException ex) {
                setStatus("⚠  Please enter a valid numeric ID.", DANGER);
            }
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnRow(btn), BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TABLE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Employee> all = manager.getAllEmployees();
        for (Employee emp : all) {
            tableModel.addRow(new Object[]{
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getDepartment(),
                emp.getTitle(),
                emp.toCSV().split(",")[5],   // startDate
                emp.toCSV().split(",")[6],   // termDate
                emp.toCSV().split(",")[7]    // active
            });
        }
        setStatus("  " + all.size() + " employee(s) loaded.", TEXT_MUTED);
    }

    /** Highlight the row whose ID column matches the given id. */
    private void highlightRow(int id) {
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            if ((int) tableModel.getValueAt(r, 0) == id) {
                employeeTable.setRowSelectionInterval(r, r);
                employeeTable.scrollRectToVisible(employeeTable.getCellRect(r, 0, true));
                return;
            }
        }
    }

    /** Filter table rows in-place (no TableRowSorter needed). */
    private void applyFilter(String text) {
        String q = text.trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Employee emp : manager.getAllEmployees()) {
            String[] csv = emp.toCSV().split(",", -1);
            String row = String.join(" ", csv).toLowerCase();
            if (q.isEmpty() || row.contains(q)) {
                tableModel.addRow(new Object[]{
                    emp.getId(), emp.getFirstName(), emp.getLastName(),
                    emp.getDepartment(), emp.getTitle(),
                    csv[5], csv[6], csv[7]
                });
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  STYLE / COMPONENT HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private void styleTable(JTable t) {
        t.setFont(FONT_TABLE);
        t.setForeground(TEXT_PRIMARY);
        t.setBackground(BG_DARK);
        t.setGridColor(BORDER_CLR);
        t.setRowHeight(28);
        t.setSelectionBackground(new Color(55, 80, 120));
        t.setSelectionForeground(TEXT_PRIMARY);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(12, 0));

        // Header
        JTableHeader header = t.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(BG_PANEL);
        header.setForeground(ACCENT);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        header.setReorderingAllowed(false);

        // Alternate row renderer
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setFont(FONT_TABLE);
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BG_DARK : BG_ROW_ALT);
                }
                // Color the "Active" column
                if (col == 7 && value != null) {
                    boolean active = Boolean.parseBoolean(value.toString());
                    setForeground(active ? SUCCESS : DANGER);
                    setText(active ? "✔ Active" : "✘ Inactive");
                }
                return this;
            }
        });

        // Column widths
        int[] widths = {55, 110, 110, 130, 140, 100, 100, 100};
        for (int i = 0; i < widths.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void styleTextField(JTextField f) {
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_PRIMARY);
        f.setBackground(BG_DARK);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private JTextField styledField() {
        JTextField f = new JTextField(20);
        styleTextField(f);
        return f;
    }

    private JButton accentButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setForeground(BG_DARK);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    /** Sidebar section label. */
    private JLabel sideLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    /** Sidebar action button — full width. */
    private JButton sideBtn(String text, Color color, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setForeground(color);
        btn.setBackground(BG_PANEL);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(listener);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(BG_ROW_ALT); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(BG_PANEL);   }
        });
        return btn;
    }

    /**
     * Build a labeled form grid from alternating label/field varargs.
     * Usage: formPanel("Label", field, "Label2", field2, ...)
     */
    private JPanel formPanel(Object... pairs) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 4, 6, 4);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < pairs.length; i += 2) {
            JLabel lbl = new JLabel((String) pairs[i] + ":");
            lbl.setFont(FONT_LABEL);
            lbl.setForeground(TEXT_MUTED);

            gc.gridx = 0; gc.gridy = i / 2; gc.weightx = 0;
            p.add(lbl, gc);

            gc.gridx = 1; gc.weightx = 1;
            p.add((Component) pairs[i + 1], gc);
        }
        return p;
    }

    /** Wraps a button in a right-aligned south panel. */
    private JPanel btnRow(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        p.setBackground(BG_PANEL);
        p.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));
        p.add(btn);
        return p;
    }

    /** 
     * Create a styled modal dialog. 
     * */
    private JDialog createDialog(String title, int w, int h) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(w, h);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(BG_PANEL);
        dlg.getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        return dlg;
    }

    private boolean anyBlank(JTextField... fields) {
        for (JTextField f : fields) if (f.getText().isBlank()) return true;
        return false;
    }

    private void setStatus(String msg, Color color) {
        statusBar.setText(msg);
        statusBar.setForeground(color);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Use system look-and-feel as a base, then override colors
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(EmployeeGUI::new);
    }
}
