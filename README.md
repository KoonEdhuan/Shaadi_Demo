🛠 Tech Stack:

UI: Jetpack Compose (Material 3)
Architecture: MVVM (Model-View-ViewModel) + Single Source of Truth
Database: Room (SQLite)
Networking: Retrofit + OkHttp
Image Loading: Glide (Compose Integration)
Concurrency: Kotlin Coroutines & Flow


In the context of a matchmaking platform, Religion and Education were selected as primary filters for the following reasons:

Education level often correlates with lifestyle and professional compatibility, while religion remains a 
significant cultural and personal preference in long-term relationship matching.


⚡ Key Features:

Offline-First: All API data is cached in Room; users can view matches even without an internet connection.
Overlay Filters: A floating filter menu that utilizes AnimatedVisibility to overlap content without disrupting the pager's position.
Vertical Pager: Used a vertical pager instead of using Lazy Column to minimize the amount swipe gestures required per profile. This also
reduced the amount of distractions user has when looking a profile.
Real-time Network Monitoring: A NetworkObserver provides a sticky "No Internet" banner that reacts instantly to connectivity changes.


🚀 Significant Future Improvement: Smart Match Algorithm

Instead of just showing matches in the order they arrive from the API, I would implement a weighted scoring algorithm 
within the Room database. Every time a user "Accepts" a profile with a specific attribute (e.g., "Master's Degree" or "Engineering"),
the app would increment a "Preference Score" for that attribute locally. This transforms the app from a static list
into a personalized experience that "learns" user tastes without requiring a complex backend ML model.
