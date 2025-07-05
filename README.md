# ETicaret 🛒📱
ETicaret is a modern Android e-commerce application that provides users with a seamless shopping experience. Users can browse products, add items to cart, manage quantities, and enjoy a smooth shopping journey with beautiful animations and intuitive UI.

## Features
- **Product Browsing**: Browse products with grid layout and search functionality
- **Category Filtering**: Filter products by categories with emoji-enhanced chips
- **Smart Sorting**: Sort products by price (ascending/descending) and name (A-Z/Z-A)
- **Cart Management**: Add products to cart with quantity selection
- **Quantity Controls**: Interactive quantity management with +/- buttons in cart
- **Empty Cart Experience**: Beautiful Lottie animations and call-to-action for empty cart
- **Product Details**: Detailed product view with image optimization
- **Modern UI**: Material Design components with smooth animations
- **Error Handling**: Robust error handling with graceful degradation
- **Offline Support**: Defensive programming for network issues

## Library Versions
| Library | Version |
| ----------------- | ----------------- |
| AndroidX Core KTX | 1.12.0 |
| AndroidX AppCompat | 1.6.1 |
| Material Design | 1.11.0 |
| Lifecycle ViewModel | 2.6.2 |
| Lifecycle LiveData | 2.6.2 |
| Navigation Component | 2.7.7 |
| Retrofit | 2.9.0 |
| Gson Converter | 2.9.0 |
| Coroutines Android | 1.7.3 |
| Glide | 4.16.0 |
| Lottie | 6.3.0 |

## Screenshots

<table>
  <tr>
    <th>Product List Screen</th>
    <th>Product Detail Screen</th>
    <th>Cart Screen</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/cbb23fc0-9b49-4bfe-89eb-59725f768c84" height="500" alt="Product List"></td>
    <td><img src="https://github.com/user-attachments/assets/25573b65-664e-440c-9c49-51b57fe77795" height="500" alt="Product Detail"></td>
    <td><img src="https://github.com/user-attachments/assets/974b5b48-5f94-4b30-90a9-38750df0a20b" height="500" alt="Cart Screen"></td>

  </tr>
</table>

## Demo Video
[![ETicaret Demo](https://github.com/user-attachments/assets/a0b6cc1e-3b81-4271-8308-5749d4d7855e)](https://www.youtube.com/shorts/ptuaDUdYPSs)

## Architecture
- **MVVM Pattern**: Clean separation of concerns with ViewModel and LiveData
- **Repository Pattern**: Centralized data access layer
- **Navigation Component**: Type-safe navigation between screens
- **Coroutines**: Asynchronous operations with proper error handling
- **Resource Wrapper**: Sealed class for handling loading, success, and error states

## Key Features Implementation

### Product Management
- Grid layout with responsive design
- Search functionality with real-time filtering
- Category filtering with emoji-enhanced chips
- Multiple sorting options (price, name)
- Image loading with Glide and error handling

### Cart Functionality
- Interactive quantity controls (+/- buttons)
- Confirmation dialog for item removal
- Real-time total calculation
- Empty cart state with Lottie animation
- Quantity update with API integration

### API Integration
- RESTful API communication with Retrofit
- Defensive programming for network issues
- Graceful error handling and fallbacks
- JSON parsing with Gson

## Requirements
- **Android Device**: Minimum Android 7.0 (API 24)
- **Internet Permission**: Required for product data and cart operations
- **Target SDK**: 35 (Android 15)

## Technologies
- **Minimum SDK**: 24
- **Target SDK**: 35
- **Programming Language**: Kotlin
- **Architecture**: MVVM
- **UI Framework**: XML Layouts with Material Design
- **Navigation**: Navigation Component
- **Image Loading**: Glide
- **Animations**: Lottie
- **Networking**: Retrofit + Coroutines

## Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run the application

## API Endpoints
- `tumUrunleriGetir.php` - Get all products
- `sepettekiUrunleriGetir.php` - Get cart items
- `sepeteUrunEkle.php` - Add product to cart
- `sepettenUrunSil.php` - Remove product from cart

## Project Structure
```
ETicaret/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/eticaret/
│   │   │   ├── data/
│   │   │   │   ├── model/          # Data models
│   │   │   │   ├── remote/         # API service and client
│   │   │   │   └── repository/     # Data access layer
│   │   │   ├── ui/
│   │   │   │   ├── productlist/    # Product list screen
│   │   │   │   ├── productdetail/  # Product detail screen
│   │   │   │   └── cart/           # Cart management
│   │   │   └── util/               # Utility classes
│   │   └── res/
│   │       ├── layout/             # UI layouts
│   │       ├── navigation/         # Navigation graphs
│   │       └── raw/                # Lottie animations
└── build.gradle.kts
```

