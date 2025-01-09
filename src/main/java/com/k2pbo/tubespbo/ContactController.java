package com.k2pbo.tubespbo;

import java.util.List;

import com.k2pbo.tubespbo.Contact.Category;
import com.k2pbo.tubespbo.DAO.ContactDao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContactController {
    @FXML private TableView<Contact> contactTable;
    @FXML private TableColumn<Contact, String> nameColumn, phoneColumn, emailColumn, addressColumn;
    @FXML private TableColumn<Contact, Contact.Category> categoryColumn;
    @FXML private TableColumn<Contact, Boolean> favoriteColumn;
    
    @FXML private TextField nameField, phoneField, emailField, addressField, searchField;
    @FXML private TextArea notesArea;
    @FXML private ComboBox<Contact.Category> categoryCombo, categoryFilter;
    @FXML private Button favoriteButton, favoriteFilter;
    
    private boolean showingFavorites = false;
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private FilteredList<Contact> filteredContacts;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadContactsFromDatabase(); 
        setupComboBoxes();
        setupSearch();
        setupFilters();
        
        // Initialize filtered list
        filteredContacts = new FilteredList<>(contacts);
        contactTable.setItems(filteredContacts);
        
        // Setup selection listener
        contactTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            } else {
                clearFields();
            }
        });
    }
    private void loadContactsFromDatabase() {
        ContactDao contactDao = new ContactDao();
        List<Contact> contactList = contactDao.getAllContacts();  // Ambil semua kontak dari database
        contacts.clear();  // Bersihkan data sebelumnya
        contacts.addAll(contactList);  // Tambahkan data baru ke ObservableList

        contactTable.setItems(contacts);  // Set ObservableList ke TableView
    }

    private void setupTableColumns() {
        // Basic setup
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        favoriteColumn.setCellValueFactory(new PropertyValueFactory<>("favorite"));

        // Set column widths and properties
        favoriteColumn.setResizable(false);
        favoriteColumn.setMaxWidth(40);
        favoriteColumn.setMinWidth(40);

        // Bind column widths to table width
        contactTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double remainingWidth = width - favoriteColumn.getWidth();
            
            nameColumn.setPrefWidth(remainingWidth * 0.25);
            phoneColumn.setPrefWidth(remainingWidth * 0.15);
            emailColumn.setPrefWidth(remainingWidth * 0.25);
            addressColumn.setPrefWidth(remainingWidth * 0.25);
            categoryColumn.setPrefWidth(remainingWidth * 0.10);
        });

        // Enable text wrapping for all columns
        nameColumn.setStyle("-fx-wrap-text: true");
        phoneColumn.setStyle("-fx-wrap-text: true");
        emailColumn.setStyle("-fx-wrap-text: true");
        addressColumn.setStyle("-fx-wrap-text: true");
        categoryColumn.setStyle("-fx-wrap-text: true");

        // Set up cell factories for text wrapping
        setupColumnCellFactories();
    }

    private void setupColumnCellFactories() {
        // Setup text columns with custom cell factory
        nameColumn.setCellFactory(tc -> createStyledCell());
        phoneColumn.setCellFactory(tc -> createStyledCell());
        emailColumn.setCellFactory(tc -> createStyledCell());
        addressColumn.setCellFactory(tc -> createStyledCell());
        
        // Setup category column
        categoryColumn.setCellFactory(tc -> new TableCell<Contact, Contact.Category>() {
            @Override
            protected void updateItem(Contact.Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
        
        // Setup favorite column
        favoriteColumn.setCellFactory(tc -> new TableCell<Contact, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "⭐" : "☆");
                    setStyle("-fx-text-fill: #ffd700;");
                }
            }
        });
    }

    private TableCell<Contact, String> createStyledCell() {
        TableCell<Contact, String> cell = new TableCell<Contact, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-wrap-text: true;");
                    setWrapText(true);
                    // Set minimum height untuk cell
                    setMinHeight(40);
                }
            }
        };
        return cell;
    }

    private void setupComboBoxes() {
        categoryCombo.setItems(FXCollections.observableArrayList(Contact.Category.values()));
        categoryFilter.setItems(FXCollections.observableArrayList(Contact.Category.values()));
    }

    private void setupSearch() {
        filteredContacts = new FilteredList<>(contacts);
        contactTable.setItems(filteredContacts);
    
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredContacts.setPredicate(contact -> {
                if (newValue == null || newValue.isEmpty()) return true;
    
                String searchText = newValue.toLowerCase();
                return contact.getName().toLowerCase().contains(searchText) ||
                       contact.getPhone().toLowerCase().contains(searchText) ||
                       contact.getEmail().toLowerCase().contains(searchText) ||
                       contact.getAddress().toLowerCase().contains(searchText) ||
                       contact.getNotes().toLowerCase().contains(searchText);
            });
        });
    }    

    private void populateFields(Contact contact) {
        nameField.setText(contact.getName());
        phoneField.setText(contact.getPhone());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());
        categoryCombo.setValue(contact.getCategory());
        notesArea.setText(contact.getNotes());
        updateFavoriteButton(contact.isFavorite());
    }


    private void setupFilters() {
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        favoriteFilter.setOnAction(event -> toggleFavoriteFilter());
    }

    @FXML
    protected void toggleFavoriteFilter() {
        showingFavorites = !showingFavorites;
        favoriteFilter.setText(showingFavorites ? "📋 Show All" : "⭐ Show Favorites");
        applyFilters();
    }

    private void applyFilters() {
        filteredContacts.setPredicate(contact -> {
            boolean categoryMatch = categoryFilter.getValue() == null || 
                                  contact.getCategory() == categoryFilter.getValue();
            boolean favoriteMatch = !showingFavorites || contact.isFavorite();
            return categoryMatch && favoriteMatch;
        });
    }

    @FXML
    protected void handleAdd() {
        try {
            if (isInputValid()) {
                Contact contact = new Contact(
                    nameField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    addressField.getText().trim(),
                    categoryCombo.getValue()  // Memastikan kategori tidak null
                );
                contact.setFavorite(favoriteButton.getText().equals("⭐"));
                contact.setNotes(notesArea.getText().trim());

                ContactDao contactDao = new ContactDao();
                boolean success = contactDao.addContact(contact);

                if (success) {
                    showAlert("Success", "Contact added successfully", "");
                    contacts.add(contact);  // Update tampilan lokal
                    clearFields();  // Bersihkan form input
                } else {
                    showAlert("Error", "Failed to add contact", "Please try again.");
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to add contact", e.getMessage());
        }
    }

       

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Name is required!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Phone number is required!\n";
        }
        if (categoryCombo.getValue() == null) {
            errorMessage += "Category must be selected!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Invalid Input", "Please correct the following:", errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void handleEdit() {
        Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectedContact.setName(nameField.getText());
            selectedContact.setPhone(phoneField.getText());
            selectedContact.setEmail(emailField.getText());
            selectedContact.setAddress(addressField.getText());
            selectedContact.setCategory(categoryCombo.getValue());
            selectedContact.setFavorite(favoriteButton.getText().equals("⭐"));
            selectedContact.setNotes(notesArea.getText());

            ContactDao contactDao = new ContactDao();
            boolean success = contactDao.updateContact(selectedContact.getId(), selectedContact);

            if (success) {
                showAlert("Success", "Contact updated successfully", "");
                contactTable.refresh(); // Memperbarui tampilan tabel
                clearFields();  // Bersihkan form input
            } else {
                showAlert("Error", "Failed to update contact", "Please try again.");
            }
        } else {
            showAlert("Error", "No contact selected", "Please select a contact to edit.");
        }
    }



    @FXML
    protected void handleDelete() {
        Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            ContactDao contactDao = new ContactDao();
            boolean success = contactDao.deleteContact(selectedContact.getId());

            if (success) {
                contacts.remove(selectedContact); // Hapus dari tampilan lokal
                clearFields();  // Bersihkan form input
                showAlert("Success", "Contact deleted successfully", "");
            } else {
                showAlert("Error", "Failed to delete contact", "Please try again.");
            }
        } else {
            showAlert("Error", "No contact selected", "Please select a contact to delete.");
        }
    }


    @FXML
    protected void handleClear() {
        clearFields();
    }

    @FXML
    protected void toggleFavorite() {
        boolean newValue = favoriteButton.getText().equals("☆");
        favoriteButton.setText(newValue ? "⭐" : "☆");
        
        Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectedContact.setFavorite(newValue);
            contactTable.refresh();
        }
    }

    private void updateFavoriteButton(boolean isFavorite) {
        favoriteButton.setText(isFavorite ? "⭐" : "☆");
    }

    
    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        categoryCombo.setValue(null);
        notesArea.clear();
        updateFavoriteButton(false);
    }
}