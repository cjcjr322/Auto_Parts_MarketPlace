package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.catalog;

import java.util.List;
import java.util.Optional;

public final class Catalog {

    public record Node(String title, String keyword) {}

    public record Section(String code, String title, List<Node> nodes) {}

    public static final List<Section> SECTIONS = List.of(
            new Section("engine", "Двигатель", List.of(
                    new Node("Поршни и кольца", "поршн"),
                    new Node("Прокладки двигателя", "прокладк"),
                    new Node("Ремень и цепь ГРМ", "грм"),
                    new Node("Свечи зажигания", "свеч"),
                    new Node("Форсунки", "форсунк"),
                    new Node("Масляный насос", "масляный насос"),
                    new Node("Турбина", "турбин"))),
            new Section("cooling", "Охлаждение и отопление", List.of(
                    new Node("Радиатор", "радиатор"),
                    new Node("Помпа (водяной насос)", "помп"),
                    new Node("Термостат", "термостат"),
                    new Node("Вентилятор радиатора", "вентилятор"),
                    new Node("Радиатор печки", "печк"))),
            new Section("electrics", "Электрика и свет", List.of(
                    new Node("Фары", "фар"),
                    new Node("Задние фонари", "фонар"),
                    new Node("Аккумулятор", "аккумулятор"),
                    new Node("Генератор", "генератор"),
                    new Node("Стартер", "стартер"),
                    new Node("Датчики", "датчик"))),
            new Section("interior", "Салон", List.of(
                    new Node("Сиденья", "сиден"),
                    new Node("Панель приборов", "панел"),
                    new Node("Руль", "руль"),
                    new Node("Обшивка и коврики", "коврик"),
                    new Node("Зеркала", "зеркал"))),
            new Section("transmission", "Трансмиссия", List.of(
                    new Node("Коробка передач", "коробк"),
                    new Node("Сцепление", "сцеплен"),
                    new Node("Карданный вал", "кардан"),
                    new Node("ШРУС и приводы", "шрус"),
                    new Node("Редуктор", "редуктор"))),
            new Section("exhaust", "Выхлопная система", List.of(
                    new Node("Глушитель", "глушител"),
                    new Node("Катализатор", "катализатор"),
                    new Node("Приёмная труба", "труба"),
                    new Node("Лямбда-зонд", "лямбда"))),
            new Section("body", "Кузов", List.of(
                    new Node("Капот", "капот"),
                    new Node("Двери", "двер"),
                    new Node("Крылья", "крыл"),
                    new Node("Бамперы", "бампер"),
                    new Node("Крышка багажника", "багажник"),
                    new Node("Стёкла", "стекл"))),
            new Section("chassis", "Ходовая часть и тормоза", List.of(
                    new Node("Амортизаторы", "амортизатор"),
                    new Node("Пружины", "пружин"),
                    new Node("Рычаги", "рычаг"),
                    new Node("Ступицы и подшипники", "ступиц"),
                    new Node("Рулевая рейка и тяги", "рулев"),
                    new Node("Тормозные колодки", "колодк"),
                    new Node("Тормозные диски", "диск"),
                    new Node("Суппорты", "суппорт")))
    );

    public static Optional<Section> find(String code) {
        return SECTIONS.stream().filter(s -> s.code().equals(code)).findFirst();
    }

    private Catalog() {}
}