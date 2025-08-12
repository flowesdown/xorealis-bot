package com.ovidius.botapp.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistry {

    @Value("${discord.test-guild-id:#{null}}")
    private String testGuildId;

    public void registerCommands(JDA jda) {

        var commandList = jda.updateCommands();

        if (testGuildId != null && !testGuildId.isBlank()) {
            var guild = jda.getGuildById(testGuildId);
            if (guild != null) {
                commandList = guild.updateCommands();
                System.out.println("Registering commands for test guild: " + guild.getName());
            }
        }

        commandList.addCommands(
                Commands.slash("ping", "Проверить задержку бота"),
                Commands.slash("server", "Показывает информацию о сервере"),
                Commands.slash("rule", "Показывает правило по его номеру")
                        .addOption(OptionType.STRING, "type", "Тип правил (discord, politic-x, classic-x)", true)
                        .addOption(OptionType.STRING, "number", "Номер правила (например, 1.1)", true),

                // === Комманды браков ===
                Commands.slash("propose", "Сделать предложение другому пользователю")
                        .addOption(OptionType.USER, "user", "Пользователь, которому вы делаете предложение", true),
                Commands.slash("divorce", "Расторгнуть текущий брак"),
                Commands.slash("couple", "Посмотреть профиль своей или чужой пары")
                        .addOption(OptionType.USER, "user", "Пользователь, чью пару вы хотите посмотреть (необязательно)", false),
                Commands.slash("kiss", "Поцеловать своего партнера")
                        .addOption(OptionType.USER, "partner", "Ваш партнер по браку", true),
                Commands.slash("поцеловать", "Поцеловать своего партнера")
                        .addOption(OptionType.USER, "partner", "Ваш партнер по браку", true),
                Commands.slash("чмок", "Чмокнуть своего партнера")
                        .addOption(OptionType.USER, "partner", "Ваш партнер по браку", true),
                Commands.slash("hug", "Обнять своего партнера")
                        .addOption(OptionType.USER, "partner", "Ваш партнер по браку", true),
                Commands.slash("обнять", "Обнять своего партнера")
                        .addOption(OptionType.USER, "partner", "Ваш партнер по браку", true),
                Commands.slash("couple-top", "Показать топ-10 самых долгих браков"),

                // === TOWNY COMMANDS ===
                Commands.slash("town", "Показывает информацию о городе")
                        .addOption(OptionType.STRING, "name", "Название города", true),

                Commands.slash("nation", "Показывает информацию о нации")
                        .addOption(OptionType.STRING, "name", "Название нации", true),

                Commands.slash("top", "Показывает различные топы сервера")
                        .addSubcommands(
                                new SubcommandData("towns-balance", "Топ-10 городов по балансу"),
                                new SubcommandData("towns-residents", "Топ-10 городов по жителям"),
                                new SubcommandData("nations-balance", "Топ-10 наций по балансу"),
                                new SubcommandData("nations-residents", "Топ-10 наций по жителям"),
                                new SubcommandData("nations-landsize", "Топ-10 наций по размеру")
                        ),
                Commands.slash("set-crest","Установить герб/иконку для города или нации(только для админов)")
                        .addOption(OptionType.STRING,"type","Тип: TOWN или NATION",true)
                        .addOption(OptionType.STRING,"name","Название города/нации",true)
                        .addOption(OptionType.STRING,"url","Прямая ссылка на изображение (URL)", true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        ).queue();
    }
}
