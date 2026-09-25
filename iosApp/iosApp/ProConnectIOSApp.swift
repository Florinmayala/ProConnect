import SwiftUI
import ComposeApp

struct SplashView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ controller: UIViewController, context: Context) {}
}

@main
struct ProConnectIOSApp: App {
    var body: some Scene {
        WindowGroup {
            SplashView().ignoresSafeArea(.container, edges: .bottom)
        }
    }
}
