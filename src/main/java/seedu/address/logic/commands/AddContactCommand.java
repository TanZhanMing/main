package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.autocorrectsuggestion.AutocorrectSuggestion;
import seedu.address.model.contact.Contact;
import seedu.address.storage.SuggestionsStorage;

/**
 * Adds a contact to the address book.
 */
public class AddContactCommand extends Command {

    public static final String COMMAND_WORD = "add_contact";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a contact to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New contact added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This contact already exists in the address book";

    private final Contact toAdd;

    /**
     * Creates an AddCommand to add the specified {@code FinSec}
     */
    public AddContactCommand(Contact contact) {
        requireNonNull(contact);
        toAdd = contact;
        //add contact details to the suggestions list
        SuggestionsStorage.addContactDetailsToSuggestionList(contact);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasContact(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addContact(toAdd);
        AutocorrectSuggestion addName = new AutocorrectSuggestion(toAdd.getName().toString());
        model.addAutocorrectSuggestion(addName);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddContactCommand // instanceof handles nulls
                && toAdd.equals(((AddContactCommand) other).toAdd));
    }
}
