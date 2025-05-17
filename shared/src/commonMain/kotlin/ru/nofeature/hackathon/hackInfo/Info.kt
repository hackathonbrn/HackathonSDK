package ru.nofeature.hackathon.hackInfo

import kotlinx.serialization.Serializable

@Serializable
data class Hackathon(
    val id: String,                     // Уникальный идентификатор хакатона
    val name: String,                    // Название хакатона
    val description: String,             // Описание
    val startDate: String,        // Дата и время начала
    val endDate: String,          // Дата и время окончания
    val location: Location,              // Место проведения (оффлайн/онлайн)
    val organizers: List<Organizer>,     // Организаторы
    val sponsors: List<Sponsor>,         // Спонсоры
    val prizes: List<Prize>,             // Призы
    val rules: List<String>,             // Правила участия
    val schedule: List<Event>,           // Расписание мероприятий
    val themes: List<String>,            // Тематические направления
    val registrationDeadline: String, // Дедлайн регистрации
    val maxTeamSize: Int,                // Максимальный размер команды
    val minTeamSize: Int,                // Минимальный размер команды
    val onlineParticipation: Boolean,    // Возможность онлайн-участия
)


// Место проведения
@Serializable
data class Location(
    val address: String?,                // Адрес (если оффлайн)
    val onlinePlatform: String?,         // Платформа (если онлайн)
    val isOnline: Boolean                // Онлайн/оффлайн
)

// Организатор
@Serializable
data class Organizer(
    val id: String,
    val name: String,
    val role: String,                    // Роль (например, "Главный организатор", "Координатор")
    val contact: String?                 // Контактная информация
)

// Спонсор
@Serializable
data class Sponsor(
    val id: String,
    val name: String,
)


// Приз
@Serializable
data class Prize(
    val id: String,
    val name: String,                    // Название (например, "1 место")
    val description: String,             // Описание (деньги, гаджеты и т. д.)
    val sponsorId: String?               // ID спонсора, если приз от него
)

// Мероприятие в расписании (лекция, митап, питчинг и т. д.)
@Serializable
data class Event(
    val id: String,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val speaker: String?,                // Спикер (если есть)
    val location: String?                // Место (если отличается от основного)
)

fun createSampleHackathon(): Hackathon {
    return Hackathon(
        id = "hack-2024-01",
        name = "Барнаульский Хакатон",
        description = "48 часов нон-стоп работы над своим технологическим стартапом.",
        startDate = "2025.05.16",
        endDate = "2025.05.18",
        location = Location(
            address = "Парк-отель Чайка",
            onlinePlatform = "Telegram",
            isOnline = false
        ),
        organizers = listOf(
            Organizer("org-1", "Barnaul Digital community", "Главный организатор", "https://t.me/dmcbarnaul"),
            Organizer("org-2", "Инновационный портал", "Организатор", "https://innovaltai.ru/"),
            Organizer("org-2", "Министерство экономического развития", "Организатор", "https://econom22.ru/")
        ),
        sponsors = listOf(
            Sponsor("spons-1", "Sveak"),
            Sponsor("spons-2", "Cortel"),
            Sponsor("spons-2", "Target")
        ),
        prizes = listOf(
            Prize("prize-1", "1 место", "100 000 рублей и стажировка в Google", "spons-1"),
            Prize("prize-2", "2 место", "50 000 рублей", "spons-2")
        ),
        rules = listOf(
            "Участие командами от 2 до 5 человек",
            "Запрещено использовать готовые решения"
        ),
        schedule = listOf(
            Event(
                "event-1",
                "Открытие",
                "Приветственное слово организаторов",
                "2025.05.16",
                "2025.05.16",
                "Алексей Воротынцев",
                "Главный зал"
            )
        ),
        themes = listOf("AI", "Web", "Mobile"),
        registrationDeadline = "2025.05.14",
        maxTeamSize = 5,
        minTeamSize = 2,
        onlineParticipation = true,
    )
}