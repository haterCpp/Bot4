package org.example;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class MyBot extends TelegramLongPollingBot {
    public MyBot() {
        super("6893445269:AAGByx-RxG0xRs_9jvgeE_hVTSjdFEa1rck");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            if (text.equals("/start")) {
                sendMessage(chatId, "Hello!");
            } else if (text.contains(",")) {
                String[] cryptocurrencies = text.split(",");
                StringBuilder messageBuilder = new StringBuilder();
                for (String crypto : cryptocurrencies) {
                    messageBuilder.append(getPriceMessage(crypto.trim())).append(", ");
                }
                String message = messageBuilder.toString().replaceAll(", $", "");
                sendMessage(chatId, message);
            }     else if (text.matches("^btc \\d+$")) {
                String[] parts = text.split(" ");
                int amount = Integer.parseInt(parts[1]);
                double price = CryptoPrice.spotPrice("BTC").getAmount().doubleValue();
                double btcAmount = amount / price;
                sendMessage(chatId, "You can buy " + btcAmount + " BTC for " + amount + " dollars.");
            }else if (text.matches("^eth \\d+$")) {
                String[] parts = text.split(" ");
                int amount = Integer.parseInt(parts[1]);
                double price = CryptoPrice.spotPrice("ETH").getAmount().doubleValue();
                double btcAmount = amount / price;
                sendMessage(chatId, "You can buy " + btcAmount + " ETH for " + amount + " dollars.");
            }else if (text.matches("^doge \\d+$")) {
                String[] parts = text.split(" ");
                int amount = Integer.parseInt(parts[1]);
                double price = CryptoPrice.spotPrice("DogeCoins").getAmount().doubleValue();
                double btcAmount = amount / price;
                sendMessage(chatId, "You can buy " + btcAmount + " DogeCoins for " + amount + " dollars.");
            }
            else if (text.equals("btc")) {
                sendPicture(chatId, "Bitcoin.png");
                sendPrice(chatId, "BTC");
            } else if (text.equals("eth")) {
                sendPicture(chatId, "Ethereum.png");
                sendPrice(chatId, "ETH");
            } else if (text.equals("doge")) {
                sendPicture(chatId, "Dogecoin.png");
                sendPrice(chatId, "Doge");
            } else if (text.equals("all")) {
                sendPrice(chatId, "BTC");
                sendPrice(chatId, "ETH");
                sendPrice(chatId, "Doge");
            } else {
                sendMessage(chatId, "Unknown command!");
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    String getPriceMessage(String name) throws Exception {
        var price = CryptoPrice.spotPrice(name);
        return name + " price: " + price.getAmount().doubleValue();
    }
    void sendPicture(long chatId, String name) throws Exception {
        var photo = getClass().getClassLoader().getResourceAsStream(name);

        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(photo, name));
        execute(message);
    }
    void sendPrice(long chatId, String name) throws Exception {
        var priceMessage = getPriceMessage(name);
        sendMessage(chatId, priceMessage);
    }

    void sendMessage(long chatId, String text) throws Exception {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    @Override
    public String getBotUsername() {

        return "Crying_java_4_bot";
    }
}
