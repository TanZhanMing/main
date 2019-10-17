package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.autocorrectsuggestion.AutocorrectSuggestion;
import seedu.address.model.claim.Claim;
import seedu.address.model.contact.Contact;
import seedu.address.model.income.Income;
import seedu.address.ui.UiManager;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FinSec finSec;
    private final UserPrefs userPrefs;
    private final FilteredList<Contact> filteredContacts;
    private final FilteredList<Claim> filteredClaims;
    private final FilteredList<Income> filteredIncomes;
    private final FilteredList<AutocorrectSuggestion> filteredSuggestions;

    /**
     * Initializes a ModelManager with the given finSec and userPrefs.
     */
    public ModelManager(ReadOnlyFinSec finSec, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(finSec, userPrefs);

        logger.fine("Initializing with address book: " + finSec + " and user prefs " + userPrefs);

        this.finSec = new FinSec(finSec);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredContacts = new FilteredList<>(this.finSec.getContactList());
        filteredClaims = new FilteredList<>(this.finSec.getClaimList());
        filteredIncomes = new FilteredList<>(this.finSec.getIncomeList());
        filteredSuggestions = new FilteredList<>(this.finSec.getAutocorrectSuggestionList());
    }

    public ModelManager() {
        this(new FinSec(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getFinSecFilePath() {
        return userPrefs.getFinSecFilePath();
    }

    @Override
    public void setFinSecFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setFinSecFilePath(addressBookFilePath);
    }

    //=========== FinSec ================================================================================

    @Override
    public void setFinSec(ReadOnlyFinSec finSec) {
        this.finSec.resetData(finSec);
    }

    @Override
    public ReadOnlyFinSec getFinSec() {
        return finSec;
    }

    @Override
    public boolean hasContact(Contact contact) {
        requireNonNull(contact);
        return finSec.hasContact(contact);
    }

    @Override
    public void deleteContact(Contact target) {
        finSec.removeContact(target);
    }

    @Override
    public void addContact(Contact contact) {
        finSec.addContact(contact);
        updateFilteredContactList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setContact(Contact target, Contact editedContact) {
        requireAllNonNull(target, editedContact);

        finSec.setContact(target, editedContact);
    }

    //=========== Claims ================================================================================
    @Override
    public boolean hasClaim(Claim claim) {
        requireNonNull(claim);
        return finSec.hasClaim(claim);
    }

    @Override
    public void deleteClaim(Claim target) {
        finSec.removeClaim(target);
    }

    @Override
    public void addClaim(Claim claim) {
        finSec.addClaim(claim);
        updateFilteredClaimList(PREDICATE_SHOW_ALL_CLAIMS);
    }

    @Override
    public void setClaim(Claim target, Claim editedClaim) {
        requireAllNonNull(target, editedClaim);

        finSec.setClaim(target, editedClaim);
    }

    @Override
    public void approveClaim(Claim claim) {
        requireNonNull(claim);
        finSec.approveClaim(claim);
    }

    @Override
    public void rejectClaim(Claim claim) {
        requireNonNull(claim);
        finSec.rejectClaim(claim);
    }

    //=========== Suggestions ================================================================================
    @Override
    public boolean hasAutocorrectSuggestion(AutocorrectSuggestion suggestion) {
        return finSec.hasAutocorrectSuggestion(suggestion);
    }

    @Override
    public void deleteAutocorrectSuggestion(AutocorrectSuggestion target) {
        finSec.removeAutocorrectSuggestion(target);
    }

    @Override
    public void addAutocorrectSuggestion(AutocorrectSuggestion suggestion) {
        finSec.addAutocorrectSuggestion(suggestion);
        updateFilteredAutocorrectSuggestionList(PREDICATE_SHOW_ALL_AUTOCORRECTSUGGESTIONS);
    }

    @Override
    public void setAutocorrectSuggestion(AutocorrectSuggestion target, AutocorrectSuggestion editedSuggestion) {
        finSec.setAutocorrectSuggestion(target, editedSuggestion);
    }

    //=========== Incomes ================================================================================
    @Override
    public boolean hasIncome(Income income) {
        requireNonNull(income);
        return finSec.hasIncome(income);
    }

    @Override
    public void deleteIncome(Income target) {
        finSec.removeIncome(target);
    }

    @Override
    public void addIncome(Income income) {
        finSec.addIncome(income);
        updateFilteredIncomeList(PREDICATE_SHOW_ALL_INCOMES);
    }

    @Override
    public void setIncome(Income target, Income editedIncome) {
        requireAllNonNull(target, editedIncome);

        finSec.setIncome(target, editedIncome);
    }



    //=========== Filtered FinSec List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code FinSec} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Contact> getFilteredContactList() {
        return filteredContacts;
    }

    @Override
    public void updateFilteredContactList(Predicate<Contact> predicate) {
        requireNonNull(predicate);
        filteredContacts.setPredicate(predicate);
        UiManager.startWithContacts();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return finSec.equals(other.finSec)
                && userPrefs.equals(other.userPrefs)
                && filteredContacts.equals(other.filteredContacts);
    }

    //=========== Filtered Claims List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Claim} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Claim> getFilteredClaimList() {
        return filteredClaims;
    }

    @Override
    public void updateFilteredClaimList(Predicate<Claim> predicate) {
        requireNonNull(predicate);
        filteredClaims.setPredicate(predicate);
        UiManager.startWithClaims();
    }

    //=========== Filtered Income List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Income} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Income> getFilteredIncomeList() {
        return filteredIncomes;
    }

    @Override
    public void updateFilteredIncomeList(Predicate<Income> predicate) {
        requireNonNull(predicate);
        filteredIncomes.setPredicate(predicate);
        UiManager.startWithIncomes();
    }

    //=========== Filtered suggestions list accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Income} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<AutocorrectSuggestion> getFilteredAutocorrectSuggestionList() {
        return getFilteredAutocorrectSuggestionList();
    }

    @Override
    public void updateFilteredAutocorrectSuggestionList(Predicate<AutocorrectSuggestion> predicate) {
        filteredSuggestions.setPredicate(predicate);
    }

}
