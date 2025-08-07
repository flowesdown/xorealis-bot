package com.ovidius.botapp.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistry {
    public void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
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

                // --- Синонимы ---
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

                Commands.slash("couple-top", "Показать топ-10 самых долгих браков")
        ).queue();
    }
}
