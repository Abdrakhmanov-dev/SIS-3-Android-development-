# Соответствие SIS 3

| Требование | Где реализовано |
|---|---|
| 3 экрана | ListScreen, DetailScreen, FavoritesScreen в ui/Screens.kt |
| LazyColumn, 10+ элементов | 12 Place в Places.items; items(visible) в ListScreen |
| LazyRow | Категории в каталоге, теги в подробностях |
| Scaffold + TopAppBar у всех | Scaffold на каждом экране, общий AppTopBar |
| Рабочая кнопка назад | onBack → navController.popBackStack() |
| Длинные заголовки | maxLines + TextOverflow.Ellipsis в карточках и подробностях |
| Пустые списки | EmptyState в каталоге и избранном |
| Image из ресурсов + описание | ScenicImage → painterResource(R.drawable.mountains), осмысленный contentDescription |
| Собственная палитра | LightColors / DarkColors в Theme.kt |
| Нет цветов и fontSize в экранах | colorScheme и typography; значения цветов и sp только в Theme.kt |
| Единые отступы | Spacing: 4, 8, 16, 24, 32 dp; отдельно 48 dp для touch target |
| Тёмная тема | AlmatyTheme → isSystemInDarkTheme() |
| Нажимаемые элементы >=48dp | IconButton.size(Spacing.touch); chips/buttons.heightIn(min=Spacing.touch) |
| 3+ компонента, каждый переиспользован | PlaceCard: каталог/избранное; SectionHeader: все экраны; TagChip: каталог/подробности; также AppTopBar, ScenicImage, EmptyState |
| Preview экранов и компонентов | 8 экранных и 7 компонентных превью, включая тёмные |
| Kotlin data class + list | data/Place.kt |
| Navigation Compose | MainActivity: NavHost + composable |
| ID как аргумент | detail/{id}, NavType.IntType → Places.find(id) |
| remember + mutableStateOf | фильтр категории, раскрываемый совет, общий state избранного |
| README, AI_USAGE | файлы в корне |
| Скриншоты каждой темы | screenshots/, проверка в VALIDATION.md |
| Макеты до кода | design/ — первый локальный коммит; формат/авторство требуют личной доработки, см. README |
| 5+ коммитов за неделю/две | Локальные коммиты за один день; календарное требование не выполнено |
| Публичный GitHub | Опубликован: https://github.com/Abdrakhmanov-dev/SIS-3-Android-development- |
| Защита | DEFENSE.md — подготовка; пройти должен студент |
| Бонус: анимация | AnimatedVisibility для совета |

Намеренно нет bottom navigation, планшетного master-detail и загруженного Google Font: это необязательные бонусы, а не основные требования.
