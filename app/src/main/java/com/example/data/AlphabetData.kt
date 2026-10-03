package com.example.data

data class WordItem(
    val word: String,
    val emoji: String,
    val description: String,
    val sentence: String
)

data class LetterInfo(
    val char: Char,
    val lowercase: Char = char.lowercaseChar(),
    val phonicsSound: String,
    val phonicsSpelling: String,
    val words: List<WordItem>,
    val rhyme: String,
    val colorHex: Long,
    val strokeGuide: String
)

object AlphabetRepository {
    val letters: List<LetterInfo> = listOf(
        LetterInfo(
            char = 'A',
            phonicsSound = "ah",
            phonicsSpelling = "/æ/ as in Apple",
            words = listOf(
                WordItem("Apple", "🍎", "Sweet and crunchy red fruit", "A is for sweet, crunchy Apple!"),
                WordItem("Ant", "🐜", "Tiny and very strong insect", "The tiny Ant carries a big leaf!"),
                WordItem("Airplane", "✈️", "Big flying machine in the clouds", "The Airplane zooms high in the sky!")
            ),
            rhyme = "A is for Apple, red and sweet,\nThe best healthy snack you can eat!",
            colorHex = 0xFFFF5964,
            strokeGuide = "1. Slant down left\n2. Slant down right\n3. Cross in the middle"
        ),
        LetterInfo(
            char = 'B',
            phonicsSound = "buh",
            phonicsSpelling = "/b/ as in Ball",
            words = listOf(
                WordItem("Ball", "⚽", "Round toy for bouncing and kicking", "B is for Ball, bounce it high!"),
                WordItem("Bear", "🐻", "Big cuddly furry friend", "The brown Bear loves golden honey!"),
                WordItem("Boat", "⛵", "Floats happily across the sea", "The sailboat glides over the waves!")
            ),
            rhyme = "B is for Ball, bounce it with glee,\nHigh in the air for all to see!",
            colorHex = 0xFFFF9233,
            strokeGuide = "1. Line down\n2. Curve around top\n3. Curve around bottom"
        ),
        LetterInfo(
            char = 'C',
            phonicsSound = "kuh",
            phonicsSpelling = "/k/ as in Cat",
            words = listOf(
                WordItem("Cat", "🐱", "Cute furry pet saying Meow", "C is for Cat, softly purring!"),
                WordItem("Car", "🚗", "Vroom vroom on the road", "The shiny Car drives down the street!"),
                WordItem("Cake", "🎂", "Yummy sweet birthday treat", "A birthday Cake with colorful sprinkles!")
            ),
            rhyme = "C is for Cat with whiskers so neat,\nPouncing on yarn with soft little feet!",
            colorHex = 0xFFFFB703,
            strokeGuide = "1. Start at top right and curve all the way around to the bottom!"
        ),
        LetterInfo(
            char = 'D',
            phonicsSound = "duh",
            phonicsSpelling = "/d/ as in Dog",
            words = listOf(
                WordItem("Dog", "🐶", "Loyal puppy wagging its tail", "D is for Dog, friendly and playful!"),
                WordItem("Duck", "🦆", "Swims in pond saying Quack", "The yellow Duck swims in the pond!"),
                WordItem("Drum", "🥁", "Fun musical beat maker", "Play the Drum with a rat-a-tat-tat!")
            ),
            rhyme = "D is for Dog who loves to play,\nChasing the stick all through the day!",
            colorHex = 0xFF06D6A0,
            strokeGuide = "1. Straight line down\n2. Big happy belly curve from top to bottom"
        ),
        LetterInfo(
            char = 'E',
            phonicsSound = "eh",
            phonicsSpelling = "/e/ as in Elephant",
            words = listOf(
                WordItem("Elephant", "🐘", "Gentle giant with a long trunk", "E is for Elephant, spraying cool water!"),
                WordItem("Egg", "🥚", "Smooth oval breakfast food", "The little bird hatches from an Egg!"),
                WordItem("Earth", "🌍", "Our beautiful round blue home", "We love and care for Planet Earth!")
            ),
            rhyme = "E is for Elephant, strong and grand,\nThe biggest friendly giant in the land!",
            colorHex = 0xFF118AB2,
            strokeGuide = "1. Line down\n2. Top line across\n3. Middle line across\n4. Bottom line across"
        ),
        LetterInfo(
            char = 'F',
            phonicsSound = "fff",
            phonicsSpelling = "/f/ as in Fish",
            words = listOf(
                WordItem("Fish", "🐟", "Swims underwater with shiny fins", "F is for Fish swimming in the deep!"),
                WordItem("Frog", "🐸", "Green jumper saying Ribbit", "The green Frog hops onto a lily pad!"),
                WordItem("Flower", "🌸", "Smells sweet in the garden", "A pretty Flower blooming in the sun!")
            ),
            rhyme = "F is for Fish in the ocean blue,\nSplashing and swimming just for you!",
            colorHex = 0xFF4361EE,
            strokeGuide = "1. Line down\n2. Top line across\n3. Middle line across"
        ),
        LetterInfo(
            char = 'G',
            phonicsSound = "guh",
            phonicsSpelling = "/g/ as in Giraffe",
            words = listOf(
                WordItem("Giraffe", "🦒", "Tall animal with very long neck", "G is for Giraffe eating green leaves!"),
                WordItem("Grapes", "🍇", "Juicy purple bunches of fruit", "Sweet Grapes fresh from the vine!"),
                WordItem("Guitar", "🎸", "Strumming happy cheerful music", "Strum the Guitar and sing along!")
            ),
            rhyme = "G is for Giraffe, stretching so high,\nReaching the leaves up in the sky!",
            colorHex = 0xFF7209B7,
            strokeGuide = "1. Big curve like C\n2. Up to middle\n3. Short line across inside"
        ),
        LetterInfo(
            char = 'H',
            phonicsSound = "huh",
            phonicsSpelling = "/h/ as in Hat",
            words = listOf(
                WordItem("Hat", "🎩", "Keeps your head sunny and warm", "H is for Hat to wear outside!"),
                WordItem("Horse", "🐴", "Gallops fast saying Neigh", "The strong Horse gallops across the meadow!"),
                WordItem("House", "🏠", "Cozy warm place where family lives", "Our cozy House is filled with love!")
            ),
            rhyme = "H is for Hat on a sunny day,\nPut it on top and go out to play!",
            colorHex = 0xFFF72585,
            strokeGuide = "1. Left line down\n2. Right line down\n3. Bridge across the middle"
        ),
        LetterInfo(
            char = 'I',
            phonicsSound = "ih",
            phonicsSpelling = "/ɪ/ as in Igloo",
            words = listOf(
                WordItem("Igloo", "🛖", "Cool snowy dome house", "I is for Igloo made of white snow!"),
                WordItem("Ice Cream", "🍦", "Cold and sweet yummy dessert", "A delicious cone of strawberry Ice Cream!"),
                WordItem("Island", "🏝️", "Tropical land surrounded by sea", "A sunny Island with palm trees!")
            ),
            rhyme = "I is for Ice Cream, tasty and cold,\nThe yummiest treat for young and old!",
            colorHex = 0xFF3A86FF,
            strokeGuide = "1. Top bar across\n2. Center line down\n3. Bottom bar across"
        ),
        LetterInfo(
            char = 'J',
            phonicsSound = "juh",
            phonicsSpelling = "/dʒ/ as in Jellyfish",
            words = listOf(
                WordItem("Jellyfish", "🪼", "Glowing swimmer in the sea", "J is for Jellyfish floating gently!"),
                WordItem("Juice", "🧃", "Refreshing sweet orange drink", "A cold box of yummy fruit Juice!"),
                WordItem("Jet", "🚀", "Super fast rocket zooming up", "The space Jet shoots to the stars!")
            ),
            rhyme = "J is for Jellyfish, floating with grace,\nDancing around in its watery place!",
            colorHex = 0xFFFF70A6,
            strokeGuide = "1. Top bar across\n2. Down the middle and scoop up left"
        ),
        LetterInfo(
            char = 'K',
            phonicsSound = "kuh",
            phonicsSpelling = "/k/ as in Kangaroo",
            words = listOf(
                WordItem("Kangaroo", "🦘", "Bouncy animal carrying baby in pouch", "K is for Kangaroo hopping high!"),
                WordItem("Kite", "🪁", "Flies dancing in the breeze", "The colorful Kite dances in the wind!"),
                WordItem("Key", "🔑", "Unlocks doors to fun secrets", "A shiny golden Key unlocks the treasure!")
            ),
            rhyme = "K is for Kangaroo, hopping around,\nWith great big leaps off the grassy ground!",
            colorHex = 0xFFFF9770,
            strokeGuide = "1. Straight line down\n2. Slant in to middle\n3. Slant down out to right"
        ),
        LetterInfo(
            char = 'L',
            phonicsSound = "lll",
            phonicsSpelling = "/l/ as in Lion",
            words = listOf(
                WordItem("Lion", "🦁", "Brave king of the jungle", "L is for Lion with a golden mane!"),
                WordItem("Lemon", "🍋", "Zesty yellow citrus fruit", "Sour yellow Lemon makes tasty lemonade!"),
                WordItem("Leaf", "🍃", "Green and fluttering on trees", "A gentle autumn Leaf drifts in the air!")
            ),
            rhyme = "L is for Lion, king of the land,\nWith a mighty roar and a golden strand!",
            colorHex = 0xFFFFD670,
            strokeGuide = "1. Straight line down\n2. Line across bottom right"
        ),
        LetterInfo(
            char = 'M',
            phonicsSound = "mmm",
            phonicsSpelling = "/m/ as in Monkey",
            words = listOf(
                WordItem("Monkey", "🐵", "Swings in trees and loves bananas", "M is for Monkey swinging happily!"),
                WordItem("Moon", "🌙", "Glowing friend in the night sky", "The silver Moon shines while we sleep!"),
                WordItem("Mango", "🥭", "Sweet and juicy tropical fruit", "A sweet ripe Mango is super yummy!")
            ),
            rhyme = "M is for Monkey, swinging through trees,\nEating bananas in the cool breeze!",
            colorHex = 0xFF2EC4B6,
            strokeGuide = "1. Line down\n2. Slant down right\n3. Slant up right\n4. Line down to bottom"
        ),
        LetterInfo(
            char = 'N',
            phonicsSound = "nnn",
            phonicsSpelling = "/n/ as in Nest",
            words = listOf(
                WordItem("Nest", "🪺", "Cozy home for little baby birds", "N is for Nest with cute little eggs!"),
                WordItem("Nut", "🌰", "Crunchy treat for squirrels", "The happy squirrel hides a brown Nut!"),
                WordItem("Nurse", "🩺", "Kind helper who heals you", "The friendly Nurse helps everyone smile!")
            ),
            rhyme = "N is for Nest where little birds sleep,\nSafe in the branches, quiet and deep!",
            colorHex = 0xFF20A4F3,
            strokeGuide = "1. Line down\n2. Slant diagonally to bottom right\n3. Line straight up"
        ),
        LetterInfo(
            char = 'O',
            phonicsSound = "ah",
            phonicsSpelling = "/ɒ/ as in Owl",
            words = listOf(
                WordItem("Owl", "🦉", "Wise night bird saying Hoot", "O is for Owl who watches in the dark!"),
                WordItem("Orange", "🍊", "Juicy round citrus snack", "Peel a sweet juicy Orange for snack!"),
                WordItem("Octopus", "🐙", "Ocean friend with 8 wiggly arms", "The Octopus has eight wiggly tentacles!")
            ),
            rhyme = "O is for Owl, so wise and bright,\nWatching the stars in the quiet night!",
            colorHex = 0xFFFF8500,
            strokeGuide = "1. Start at top and draw a complete smooth circle all the way around!"
        ),
        LetterInfo(
            char = 'P',
            phonicsSound = "puh",
            phonicsSpelling = "/p/ as in Panda",
            words = listOf(
                WordItem("Panda", "🐼", "Cute black and white bear eating bamboo", "P is for Panda, munching on bamboo!"),
                WordItem("Penguin", "🐧", "Waddles on ice and swims fast", "The cheerful Penguin waddles in the snow!"),
                WordItem("Pizza", "🍕", "Delicious cheesy warm slice", "Melted cheese on a warm slice of Pizza!")
            ),
            rhyme = "P is for Panda, black and white,\nEating sweet bamboo with pure delight!",
            colorHex = 0xFFE63946,
            strokeGuide = "1. Straight line down\n2. Curve around top half only"
        ),
        LetterInfo(
            char = 'Q',
            phonicsSound = "kwuh",
            phonicsSpelling = "/kw/ as in Queen",
            words = listOf(
                WordItem("Queen", "👑", "Wears a shiny sparkling crown", "Q is for Queen in her royal castle!"),
                WordItem("Quilt", "🪡", "Warm cozy patchwork blanket", "Cuddle under the soft colorful Quilt!"),
                WordItem("Quail", "🐦", "Cute little bird with a feather head", "The little Quail sings a gentle chirp!")
            ),
            rhyme = "Q is for Queen with a crown of gold,\nKind and gentle, noble and bold!",
            colorHex = 0xFF9B5DE5,
            strokeGuide = "1. Make a big circle like O\n2. Add a little kick out leg at bottom right"
        ),
        LetterInfo(
            char = 'R',
            phonicsSound = "rrr",
            phonicsSpelling = "/r/ as in Rainbow",
            words = listOf(
                WordItem("Rainbow", "🌈", "Beautiful arc of 7 bright colors", "R is for Rainbow shining after rain!"),
                WordItem("Rabbit", "🐰", "Hoppy friend with long soft ears", "The fluffy Rabbit hops across the grass!"),
                WordItem("Rocket", "🚀", "Zooms through space to the planets", "3, 2, 1! The Rocket blasts into space!")
            ),
            rhyme = "R is for Rainbow, arched in the sky,\nWith all the bright colors shining high!",
            colorHex = 0xFF00BBF9,
            strokeGuide = "1. Line down\n2. Loop top half\n3. Kick leg down to right"
        ),
        LetterInfo(
            char = 'S',
            phonicsSound = "sss",
            phonicsSpelling = "/s/ as in Sun",
            words = listOf(
                WordItem("Sun", "☀️", "Warm golden star shining above", "S is for Sun, bringing light and warmth!"),
                WordItem("Star", "⭐", "Twinkling diamond in the night", "Twinkle twinkle, little Star!"),
                WordItem("Strawberry", "🍓", "Sweet red berry with little seeds", "Ripe red Strawberries taste delicious!")
            ),
            rhyme = "S is for Sun with rays of light,\nWarming the day and making it bright!",
            colorHex = 0xFFFFD166,
            strokeGuide = "1. Curve left around, snake through middle, and curve right to bottom!"
        ),
        LetterInfo(
            char = 'T',
            phonicsSound = "tuh",
            phonicsSpelling = "/t/ as in Tiger",
            words = listOf(
                WordItem("Tiger", "🐯", "Striped big cat with a proud stride", "T is for Tiger with bold orange stripes!"),
                WordItem("Tree", "🌳", "Tall green giant giving fresh air", "Birds build happy homes inside the Tree!"),
                WordItem("Train", "🚂", "Choo-choo chugging on the rails", "The Train chugs down the mountain tracks!")
            ),
            rhyme = "T is for Tiger with orange stripes so bold,\nA magnificent sight for all to behold!",
            colorHex = 0xFF06D6A0,
            strokeGuide = "1. Long roof bar across top\n2. Center line down"
        ),
        LetterInfo(
            char = 'U',
            phonicsSound = "uh",
            phonicsSpelling = "/ʌ/ as in Umbrella",
            words = listOf(
                WordItem("Umbrella", "☂️", "Keeps you dry when raindrops fall", "U is for Umbrella, splash in the puddles!"),
                WordItem("Unicorn", "🦄", "Magical pony with a rainbow horn", "The magical Unicorn gallops on rainbows!"),
                WordItem("Up", "🎈", "Floating high into the blue sky", "Balloons fly Up, up, and away!")
            ),
            rhyme = "U is for Umbrella when rain pours down,\nSplashing in puddles all through the town!",
            colorHex = 0xFF8338EC,
            strokeGuide = "1. Down, curve along the bottom, and back straight up!"
        ),
        LetterInfo(
            char = 'V',
            phonicsSound = "vvv",
            phonicsSpelling = "/v/ as in Violin",
            words = listOf(
                WordItem("Violin", "🎻", "Musical strings playing sweet notes", "V is for Violin, playing lovely music!"),
                WordItem("Van", "🚐", "Roomy car for road trip adventures", "Pack the Van for a fun family trip!"),
                WordItem("Volcano", "🌋", "Mountain of rumbling hot rocks", "The Volcano stands tall and mighty!")
            ),
            rhyme = "V is for Violin, sweet to hear,\nPlaying soft tunes for every ear!",
            colorHex = 0xFFFB5607,
            strokeGuide = "1. Slant down right\n2. Slant back up right to the top"
        ),
        LetterInfo(
            char = 'W',
            phonicsSound = "wuh",
            phonicsSpelling = "/w/ as in Whale",
            words = listOf(
                WordItem("Whale", "🐳", "Friendly ocean giant with big spout", "W is for Whale, spouting high water!"),
                WordItem("Watermelon", "🍉", "Juicy green and pink summer snack", "Cool sweet Watermelon on a warm day!"),
                WordItem("Windmill", "💨", "Spins around in the breezy wind", "The Windmill spins round and round!")
            ),
            rhyme = "W is for Whale in oceans wide,\nSwimming with joy in the rising tide!",
            colorHex = 0xFF3A86FF,
            strokeGuide = "1. Slant down\n2. Slant up\n3. Slant down\n4. Slant up to top"
        ),
        LetterInfo(
            char = 'X',
            phonicsSound = "kss",
            phonicsSpelling = "/ks/ as in Xylophone",
            words = listOf(
                WordItem("Xylophone", "🎼", "Rainbow bars that chime when tapped", "X is for Xylophone, ding-dong chime!"),
                WordItem("X-ray", "🩻", "Special picture showing strong bones", "Doctor takes an X-ray to check our bones!"),
                WordItem("Xerox", "📄", "Copies paper pictures quickly", "A handy Xerox creates duplicate drawings!")
            ),
            rhyme = "X is for Xylophone, chime chime chime,\nMaking sweet music all the time!",
            colorHex = 0xFFFF006E,
            strokeGuide = "1. Slant down right\n2. Cross over: slant down left"
        ),
        LetterInfo(
            char = 'Y',
            phonicsSound = "yuh",
            phonicsSpelling = "/j/ as in Yo-yo",
            words = listOf(
                WordItem("Yo-yo", "🪀", "Spins down on a string and hops back", "Y is for Yo-yo, spinning up and down!"),
                WordItem("Yak", "🐂", "Furry horned friend in snowy hills", "The shaggy Yak walks on snowy mountain paths!"),
                WordItem("Yacht", "🛥️", "Fancy white boat cruising the sea", "The sleek Yacht glides on sparkling water!")
            ),
            rhyme = "Y is for Yo-yo, up and down,\nThe coolest spinning toy in town!",
            colorHex = 0xFFFFBE0B,
            strokeGuide = "1. Little V at the top\n2. Straight stem down from bottom of V"
        ),
        LetterInfo(
            char = 'Z',
            phonicsSound = "zzz",
            phonicsSpelling = "/z/ as in Zebra",
            words = listOf(
                WordItem("Zebra", "🦓", "Stunning wild horse with black-white stripes", "Z is for Zebra, running in the savannah!"),
                WordItem("Zip", "🤐", "Pulls jackets closed snug and warm", "Zip up your warm coat before going out!"),
                WordItem("Zoo", "🦁", "Park where animals live and play", "We saw elephants and lions at the Zoo!")
            ),
            rhyme = "Z is for Zebra with stripes so neat,\nRunning and galloping on swift feet!",
            colorHex = 0xFF06D6A0,
            strokeGuide = "1. Line across top\n2. Slant diagonally down left\n3. Line across bottom"
        )
    )

    fun getByChar(char: Char): LetterInfo? {
        return letters.find { it.char.equals(char, ignoreCase = true) }
    }
}
