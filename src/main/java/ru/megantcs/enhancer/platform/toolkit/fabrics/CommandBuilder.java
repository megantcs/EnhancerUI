package ru.megantcs.enhancer.platform.toolkit.fabrics;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Action;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandBuilder {
    private final String command;
    private final LiteralArgumentBuilder<FabricClientCommandSource> builder;
    private Action<CommandContext<FabricClientCommandSource>> defaultAction;

    protected CommandBuilder(String command) {
        this.command = command;
        this.builder = literal(command);
    }

    // Основной метод выполнения команды
    public CommandBuilder executes(Action<CommandContext<FabricClientCommandSource>> action) {
        this.defaultAction = action;
        builder.executes(context -> {
            if (defaultAction != null) {
                defaultAction.invoke(context);
            }
            return Command.SINGLE_SUCCESS;
        });
        return this;
    }

    // Добавление подкоманды без аргументов
    public CommandBuilder thenLiteral(String literal, Action<CommandContext<FabricClientCommandSource>> action) {
        builder.then(literal(literal)
                .executes(context -> {
                    action.invoke(context);
                    return Command.SINGLE_SUCCESS;
                }));
        return this;
    }

    // Добавление строкового аргумента
    public CommandBuilder thenString(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenString(argName, action, null);
    }

    public CommandBuilder thenString(String argName, Action<CommandContext<FabricClientCommandSource>> action,
                                     SuggestionProvider<FabricClientCommandSource> suggestions) {
        RequiredArgumentBuilder<FabricClientCommandSource, String> argumentBuilder =
                argument(argName, StringArgumentType.string());

        if (suggestions != null) {
            argumentBuilder.suggests(suggestions);
        }

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Добавление целочисленного аргумента
    public CommandBuilder thenInteger(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenInteger(argName, action, null, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public CommandBuilder thenInteger(String argName, Action<CommandContext<FabricClientCommandSource>> action,
                                      SuggestionProvider<FabricClientCommandSource> suggestions,
                                      int min, int max) {
        RequiredArgumentBuilder<FabricClientCommandSource, Integer> argumentBuilder =
                argument(argName, IntegerArgumentType.integer(min, max));

        if (suggestions != null) {
            argumentBuilder.suggests(suggestions);
        }

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Добавление дробного аргумента
    public CommandBuilder thenDouble(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenDouble(argName, action, null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public CommandBuilder thenDouble(String argName, Action<CommandContext<FabricClientCommandSource>> action,
                                     SuggestionProvider<FabricClientCommandSource> suggestions,
                                     double min, double max) {
        RequiredArgumentBuilder<FabricClientCommandSource, Double> argumentBuilder =
                argument(argName, DoubleArgumentType.doubleArg(min, max));

        if (suggestions != null) {
            argumentBuilder.suggests(suggestions);
        }

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Добавление логического аргумента
    public CommandBuilder thenBoolean(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        RequiredArgumentBuilder<FabricClientCommandSource, Boolean> argumentBuilder =
                argument(argName, BoolArgumentType.bool());

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Добавление аргумента из списка значений
    public CommandBuilder thenWord(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        RequiredArgumentBuilder<FabricClientCommandSource, String> argumentBuilder =
                argument(argName, StringArgumentType.word());

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Добавление аргумента с жадным парсингом (оставшийся текст)
    public CommandBuilder thenGreedyString(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        RequiredArgumentBuilder<FabricClientCommandSource, String> argumentBuilder =
                argument(argName, StringArgumentType.greedyString());

        builder.then(argumentBuilder.executes(context -> {
            action.invoke(context);
            return Command.SINGLE_SUCCESS;
        }));
        return this;
    }

    // Метод для создания цепочек аргументов
    public CommandBuilder withArgs(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenString(argName, action);
    }

    public CommandBuilder withArgs(String argName, Class<?> argType, Action<CommandContext<FabricClientCommandSource>> action) {
        if (argType == Integer.class || argType == int.class) {
            return thenInteger(argName, action);
        } else if (argType == Double.class || argType == double.class) {
            return thenDouble(argName, action);
        } else if (argType == Boolean.class || argType == boolean.class) {
            return thenBoolean(argName, action);
        } else {
            return thenString(argName, action);
        }
    }

    // Методы для добавления подсказок (suggestions)
    public CommandBuilder withSuggestions(String argName, List<String> suggestions,
                                          Action<CommandContext<FabricClientCommandSource>> action) {
        return thenString(argName, action, (context, builder) -> {
            for (String suggestion : suggestions) {
                builder.suggest(suggestion);
            }
            return builder.buildFuture();
        });
    }

    public CommandBuilder withSuggestions(String argName, Supplier<List<String>> suggestionsSupplier,
                                          Action<CommandContext<FabricClientCommandSource>> action) {
        return thenString(argName, action, (context, builder) -> {
            List<String> suggestions = suggestionsSupplier.get();
            for (String suggestion : suggestions) {
                builder.suggest(suggestion);
            }
            return builder.buildFuture();
        });
    }

    public CommandBuilder withSuggestions(String argName, String[] suggestions,
                                          Action<CommandContext<FabricClientCommandSource>> action) {
        return withSuggestions(argName, Arrays.asList(suggestions), action);
    }

    // Методы для обратной совместимости (старый API)
    public CommandBuilder orArg(String argument, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenLiteral(argument, action);
    }

    public CommandBuilder thenArg(String argument, Action<CommandContext<FabricClientCommandSource>> action) {
        return thenLiteral(argument, action);
    }

    // Вспомогательные методы
    public CommandBuilder withFeedback(String message) {
        return executes(context -> {
            context.getSource().sendFeedback(Text.literal(message));
        });
    }

    public CommandBuilder withFeedback(Supplier<String> messageSupplier) {
        return executes(context -> {
            context.getSource().sendFeedback(Text.literal(messageSupplier.get()));
        });
    }

    // Цепочки команд (более сложная структура)
    public CommandChain thenChain(String literal) {
        return new CommandChain(this, literal);
    }

    // Регистрация команды
    public CommandBuilder register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(builder);
        });
        return this;
    }

    public static CommandBuilder create(String command) {
        return new CommandBuilder(command);
    }

    // Вложенный класс для создания цепочек команд
    public static class CommandChain {
        private final CommandBuilder parent;
        private final LiteralArgumentBuilder<FabricClientCommandSource> chainBuilder;

        public CommandChain(CommandBuilder parent, String literal) {
            this.parent = parent;
            this.chainBuilder = literal(literal);
        }

        public CommandChain thenLiteral(String literal, Action<CommandContext<FabricClientCommandSource>> action) {
            chainBuilder.then(literal(literal)
                    .executes(context -> {
                        action.invoke(context);
                        return Command.SINGLE_SUCCESS;
                    }));
            return this;
        }

        public CommandChain thenString(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
            chainBuilder.then(argument(argName, StringArgumentType.string())
                    .executes(context -> {
                        action.invoke(context);
                        return Command.SINGLE_SUCCESS;
                    }));
            return this;
        }

        public CommandChain thenInteger(String argName, Action<CommandContext<FabricClientCommandSource>> action) {
            chainBuilder.then(argument(argName, IntegerArgumentType.integer())
                    .executes(context -> {
                        action.invoke(context);
                        return Command.SINGLE_SUCCESS;
                    }));
            return this;
        }

        public CommandChain executes(Action<CommandContext<FabricClientCommandSource>> action) {
            chainBuilder.executes(context -> {
                action.invoke(context);
                return Command.SINGLE_SUCCESS;
            });
            parent.builder.then(chainBuilder);
            return this;
        }

        public CommandBuilder endChain() {
            parent.builder.then(chainBuilder);
            return parent;
        }
    }

    // Утилитарные методы для получения аргументов
    public static String getString(CommandContext<FabricClientCommandSource> context, String argName) {
        return StringArgumentType.getString(context, argName);
    }

    public static int getInteger(CommandContext<FabricClientCommandSource> context, String argName) {
        return IntegerArgumentType.getInteger(context, argName);
    }

    public static double getDouble(CommandContext<FabricClientCommandSource> context, String argName) {
        return DoubleArgumentType.getDouble(context, argName);
    }

    public static boolean getBoolean(CommandContext<FabricClientCommandSource> context, String argName) {
        return BoolArgumentType.getBool(context, argName);
    }
}