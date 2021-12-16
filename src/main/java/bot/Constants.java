package bot;

public interface Constants {

    /**
     * Strings for commands replies
     */
    String START_REPLY = "Добро пожаловать! Бот предназначен для парсинга и подсчета налогов из отчетов брокера Interactive Brokers в формате htm. Для старта расчета используйте команду /calculate";
    String CALCULATE_REPLY_PART_1 = "Для старта расчета просто пришли мне отчет Interactive Brokers в формате htm. Вы получите отчет в формате ";
    String CALCULATE_REPLY_PART_2 = ". Если хотите сменить формат воспользуйтесь командой /format";
    String FORMAT_REPLY = "В каком формате хотите получать документ?";

    /**
     * Other strings
     */
    String WAIT_INCOME_FILE = "Жду отчет от Interactive brokers в формате htm";
    String USE_CALCULATE_COMMAND = "Для расчета воспользуйтесь командой /calculate";
    String PROCESSING = "Произвожу расчеты...";
    String WRONG_FILE_FORMAT = "Файл не распознан как отчет Interactive Brokers в формате htm";
    String SUBSCRIPTION_EXPIRED = "Извините период вашей подписки истек. Оплатите подписку заново.";
    String SUBSCRIPTION_STILL_VALID = "Ваша подписка активна до: ";

    /**
     * Strings for invoice
     */
    String INVOICE_ITEM_TITLE = "Подписка на 10 дней";
    String INVOICE_TITLE = "Расчет налогов для отчета IB";
    String INVOICE_DESCRIPTION = "Расчет налогов для отчета IB";
    String INVOICE_START_PARAMETER = "start";
}
