// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "Agilli",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "Agilli",
            targets: ["AgilliIntegrationPluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "AgilliIntegrationPluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AgilliIntegrationPluginPlugin"),
        .testTarget(
            name: "AgilliIntegrationPluginPluginTests",
            dependencies: ["AgilliIntegrationPluginPlugin"],
            path: "ios/Tests/AgilliIntegrationPluginPluginTests")
    ]
)