package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    //    @Test
    //    public void parse_validArgs_returnsSortCommand() throws ParseException {
    //        assertParseSuccess(parser, "name", new SortCommand(FIRST_FILTER));
    //    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format("Filter is not recognised",
                SortCommand.MESSAGE_USAGE));
    }
}