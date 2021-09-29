


             _______ .______        ______   .___  ___.     ________   _______ .______        ______   
            |   ____||   _  \      /  __  \  |   \/   |    |       /  |   ____||   _  \      /  __  \  
            |  |__   |  |_)  |    |  |  |  | |  \  /  |    `---/  /   |  |__   |  |_)  |    |  |  |  | 
            |   __|  |      /     |  |  |  | |  |\/|  |       /  /    |   __|  |      /     |  |  |  | 
            |  |     |  |\  \----.|  `--'  | |  |  |  |      /  /----.|  |____ |  |\  \----.|  `--'  | 
            |__|     | _| `._____| \______/  |__|  |__|     /________||_______|| _| `._____| \______/                                                                                                   
            .___________.  ______        ___     ___      
            |           | /  __  \      / _ \   / _ \   _ 
            `---|  |----`|  |  |  |    | (_) | | (_) | (_)
                |  |     |  |  |  |     \__, |  > _ <     
                |  |     |  `--'  |       / /  | (_) |  _ 
                |__|      \______/       /_/    \___/  (_)
                                                          

                                                       
Speed up your Testing with DaggerMock and Live Kaspresso!

by Nick DiPatri
Comcast Corporation, Philadelphia

<https://github.com/ndipatri/ZeroTo98/>




### If you are presenting this to others, here's your checklist:

1. Restart IDE! (so it doesn't OOM during talk)
2. Code checked out 

>Check out [Step 1 branch](https://github.com/ndipatri/ZeroTo98/tree/step1_workingNoTests) if skipping building the init app.

3. Code deployed to phone BUT STOP THE APP.
4. Disconnect all real devices except test device.
5. IDE open, Project panel collapsed. IDE sized to show Vizor-enabled device on the right side (emulator is too slow)
6. Turn off computer notifications
7. I recommend presenting this README on your IDE in Presentation Mode.
8. Power-up siren and put it on desk.
9. Put IDE in Presentation Mode
10. Read this:

>The prototype-to-production pipeline for mobile applications doesn’t always have the luxury of test-driven development. As a result, some production code has very ***low unit test coverage***.

>Today we’re going to learn how to use Espresso in new ways to ***quickly stand up live end-to end integration tests***.  These tests give us confidence so that we can safely move ahead with feature changes while we slowly ***build up our unit test coverage from 0 to 98 percent***, a process which can take weeks or months! 

11. ***Take IDE OUT of Presentation mode and show Project***

12. Read this:

>I was trying to think of a fun application that we could use to demonstrate the concept of going from 0 to 98 as quickly as possible. ***Since this kinda sounds like speeding to me***, I thought that using a big red police siren might be appropriate. 

>***So let's run this app and use it to control our red siren.***






# Presentation

For each of the following steps (1,2,3,4,5,6), there is a branch in this repo that represents that step with all of its code changes completed.

The instructions below contain code snippets that have to be typed and 'live template' code that is inserted using IDE shortcuts.  For these to work, you MUST 'import' the [./liveTemplatesToImport.zip](./liveTemplatesToImport.zip) file.  Importing using File -> Manage IDE Settings -> Import.

To understand how Andoid Studio Live Templates work, read [this article](https://www.jetbrains.com/help/idea/using-live-templates.html)


## Step 0 - Create ‘Basic Activity’

Begin with Android Studio and create a new ‘Basic Activity’.  Create using package/project name: "com.ndipatri.iot.zeroto98"

## Step 1 - Use Dagger to inject ParticleAPI

### 1_1 - Update project Gradle file (live template 'step1_1_gradle_project')

>For the most part, I’m not going to discuss build.gradle file changes and focus on Kotlin.  So i’m just going to quickly modify >the project's gradle files to get what we need.  You can refer to this repo later to see what gradle changes were necessary for >this work.

Open project **build.gradle** file and delete contents and {insert live template **step1_1**}


### 1_2 - Update app Gradle file (live template 'step1_2_gradle_app')

>We also need to modify the app gradle file

Open file **app/build.gradle** file and delete contents and {insert live template **step1_2**}


### 1_3 - Create ParticleAPI class (live template 'step1_3_api')

>We’re going to modify the FirstFragment so it can talk to our siren.  We’re going to let the user see the current state of
>the siren and turn the siren on and off.

>In order to do that, it will need to create an API object that can make network calls to the Particle Cloud, which is 
>how you control this siren
 
Create package **api** in **app/src/main/java/com/ndipatri/iot/zerioto98** directory.

Create **ParticleAPI.kt** class in that directory as shown:


```kotlin
class ParticleAPI {
     {insert live template step1_3}
}
```


>This ParticleAPI will be an external dependency to our FirstFragment.

>Testing is about controlling external dependencies.  The first step in doing this is using a dependency injection
>framework such as Dagger.

>Now that we’ve defined our external dependency, the ParticleAPI, we need a way to create this dependency 
>and make it available to whoever needs it in our app. 

>A Dagger Component is a singleton container which will hold our ParticleAPI object. Let’s go ahead and 
>create a Dagger Component for this app.”


### 1_4 - Create ApplicationComponent class and ApiModule (live template 'step1_4_dagger_module')

Create file **app/src/main/java/com.ndipatri.iot.zerioto98/ApplicationComponent.kt** as **AS INTERFACE**

(insert code below and THEN type the annotations at the top)

```kotlin
@Singleton
@Component(modules = [ApiModule::class])
interface ApplicationComponent {

      {insert live template step1_4} 

}
```

>We use Dagger modules to group our dependencies into various contexts.  In this case, we are putting our 
>ParticleAPI into the ‘ApiModule’. If we had more APIs defined, they would also go in this module.


### 1_5 - Create ApplicationComponent code block (live template 'step1_5_dagger_component')

>Now we build the Dagger Component that will create and distribute this Module.

Insert Dagger Component component at the top of the class as shown:


```kotlin
class ApplicationComponent {

 {insert live template step1_5} 

   @Module
   class ApiModule() {
       @Provides
       @Singleton
       fun provideParticleAPI(): ParticleAPI {
           return ParticleAPI()
       }
   }
}
```

>The ‘component’ property is instantiated the first time we call ‘createIfNecessary()’ and returned.  Notice the ‘component’ property can be re-assigned.  This will be important later when we start writing tests.  Now our ApiModule and the ParticleAPI are available to be injected into our ‘FirstFragment’”


### 1_6 - FirstFragment uses injected ParticleAPI to update view and control siren (live template 'step1_6_firstFragment')

>Our FirstFragement can now be changed to use our injected ParticleAPI to update status and control our siren.”

Open **app/src/main/java/com.ndipatri.iot.zerioto98/FirstFragment.kt**

Type the following near the top of FirstFragment.kt

```kotlin
@Inject
lateinit var particleAPI: ParticleAPI
```

>This tells Dagger to assign its ParticleAPI dependency to this local property.  We need to use our static reference to the Dagger component we created early now to ‘inject’ this FirstFragment into the Dagger dependency graph.

Type the following near the beginning of the **onCreateView()** method in FirstFragment.kt

```kotlin
ApplicationComponent.createIfNecessary().inject(this)
```

>When we make this ‘inject()’ call, the ParticleAPI property is assigned.

>We need to update this app to allow for network calls.  

Add the following to AndroidManifest.xml

```kotlin
<uses-permission android:name="android.permission.INTERNET" />
```

>Now we update this Fragment to use this injected API to query the current status of the siren and display it.  
>This Fragment also provides a button to toggle the on/off state of the siren”

Replace the following code in FirstFragment.kt with {live template step1_6}:

```kotlin
       binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
}
```

>Now we display the current state of the siren at the top of the Fragment and at the bottom of the fragment we have a toggle button.


Run the app to demonstrate!














 ![Start Here](/startHere.png)

>Jump to here!
>
>If you don't want to build the app.
>Make sure you checkout 
>[Step 1 Branch](https://github.com/ndipatri/ZeroTo98/tree/step1_workingNoTests)




## Step 2 - Write an unstable live end-to-end Espresso test


### 2_1 - Start with ActivityTestRule (live template 'step1_1_activityTestRule')

>Now let's assume our app has been deployed and distributed to many users. Let’s assume our app is ***way more complicated than what we just built and that it has NO unit tests***.  Let’s assume the app has all ***new developers who don't know the app***. 

>***How do we proceed with feature development*** when we don’t know if we are breaking the app and its many wonderful features?

>We can ***protect the entire app with a Live end-to-end Espresso Test***. The test is 'Live' becuse we run it using real accounts and real hardware.  Settings up Mocks requires knowledge we just don't have as new developers on this team.  The test is 'End to End' because we are not mocking any of our apps components.

>This can be written without ANY knowledge of the app, but it ***requires some tricks*** as we'll soon learn.

>Let’s start by writing a very simple Espresso test.


Open **app/androidTest/java/com/ndipatri/iot/zeroTo98/ExampeInstrumentedTest.kt**

Delete existing test.

Add a new **ActivityTestRule** using {insert live template **step2_1**}


```kotlin
@RunWith(AndroidJUnit4::class)
class MainScreenEspressoTest {

    {insert live template step2_1}

}
```

>The ActivityTestRule is how Espresso starts a test.



### 2_2 - Write the test method (live template 'step2_2_testMethod')

Add new ‘showCurrentRedSirenState_off’ test 

```kotlin
@RunWith(AndroidJUnit4::class)
class MainScreenEspressoTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(
        MainActivity::class.java,
        false,
        false
    )
    

    {insert live template step2_2}

}
```

>Here we have a simple test that asserts our Fragment’s views when the siren is off. 
 
>Remember with Espresso, our ***tests run in a separate thread*** from the app itself.  We need an explicit delay here because it takes a bit of ***time to query Particle for the current state of the siren***. If we knew more about the code itself, remember we’re new developers on this project, we could ‘synchronize’ this test with our app using ***Idling Resources***, but we’re trying to go from ***0 to 98 very quickly*** and don't know much about the code.  So we'll keep this explicit delay for our end-to-end test for now. 

Make sure the siren is off and run the test.


>The big problem with our live test is that it depends on the ***current state*** of our real-world siren.  If the siren is on, this test will fail.

Turn on siren and re-run test to show it fails.


## Step 3 - Kaspresso Live Test 

### 3_1 - MainScreen(live template 'step3_1_MainScreen')

>The problem is our end-to-end Espresso test is not reliable because of the unknown state of the live system we are testing against.  
>Remember, we want to be able to write this test fast and without an in-depth understanding of our code. 

>We are going to use a relatively ***new library called Kaspresso*** to help us solve this problem very quickly. “

>Kaspresso follow’s ***Martin Fowler’s (PageObject) advice on testing UI***.  He recommends we create a UI abstraction for our UI element under test.  Which, in this case, is an Activity.  

>Kaspresso calls this abstraction a ***‘Screen’***.  This Screen object defines what UI elements are available and their common actions. 

>The idea here is this abstraction ***allows us to write cleaner Espresso tests*** that are easier to write and debug.

>So let’s create a Kaspresso Screen object for our MainActivity.

Create the **app/src/androidTest/java/com/ndipatri/iot/zeroto98/MainScreen.kt** file.

Type this code in the file (NOTE: It's on ***OBJECT*** not a class!) and inject code as follows:

```kotlin
object MainScreen : KScreen<MainScreen>() {

    {insert live template step3_1}

}
```

>We’ve defined two UI abstractions in our MainScreen: a KTextView and a KButton.  From a testing perspective, these are the only two things we care about from our MainActivity.”


### 3_2 - Use Kaspresso in our Espresso Test (live template 'step3_2_Kaspresso')

>Now let’s see how we use our new MainScreen UI abstractions to make our Espresso test more reliable and easier to read!”

Open the file **app/src/androidTest/java/com/ndipatri/iot/zeroto98/ExampeInstrumentedTest.kt** 

Make ExampeInstrumentedTest ***extend Kaspresso’s TestCase()***

Delete all code in test and insert live code as shown:

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest: TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(
        MainActivity::class.java,
           false,
           false
    )

    @Test  
    fun showCurrentRedSirenState_off() {

       activityTestRule.launchActivity(Intent())


       {Delete all code from test within this area and replace 
        with live template step3_2}

    } 
}
```

>Our Kaspresso test lives inside of this new ‘run’ block and you can see we’ve set the context to MainScreen, our UI abstraction.  The big thing to ***notice is the ‘step’ block***.  This is where the magic happens.  

>Inside of this step ***we’ve added something***.  We are now changing the state of the siren, by clicking the button, regardless of the current state.  So we are ***either turning the siren on or off, we don’t know***.  Then we test to see if the siren is in the desired state, which is ‘off’. 

>If by chance, the siren is already off when we start this test, we will be turning it on and thus making the test fail.  Here is where Kaspresso comes into play.  

>If an assertion inside of any **step{} block throws an assertion exception**, the whole step will be replayed by Kaspresso.  So again, the button will be pressed, but this time the siren will be turned off and the test will succeed.  We ***keep trying until we get the desired result***.  Eventually Kaspresso does timeout and the test will fail, however.

***Uninstall the app before running test.***

Run the test and demonstrate that it succeeds regardless of the state of the siren.

>In essence, we’re using ***‘short polling’*** to wait for our external system reach a known state.  This makes our test code look much simpler and hides all the complexities of this polling mechanism inside of our Kaspresso library.  This technique is ***CPU intensive*** , but we don’t care because this is a test and ***NOT production code***.  
>The bottom line we are now testing our code and not the state of our external system.

>***Now we have live end-to-end user test coverage that is reliable. Our Android developers can now work on new features with confidence.***

>The problem with this test is ***brittle*** because it depends on ***real external dependencies*** - our siren and the Particle Cloud that controls it.  If anything breaks with these external dependencies, our test fails and we then have to investigate and waste time.

>Understand that testing is about ***controlling external dependencies***.  With this live test, we have no control over our external dependencies.  

>Let’s assume that ***some time has passed*** and we now can spend a few moments converting this from a live end-to-end test to a mock end-to-end test. We’ll need to ***mock our external dependency*** which in this case is the network call to the Particle Cloud.

>I’m still calling it an ‘end-to-end’ test because we want to ***mock NOTHING internal*** to our application, just the network call itself.

>Let’s take a ***look back at the ParticleAPI*** dependency that we’re injecting into our FirstFragment because this is where we make our network call.


## Step 4 - Look at ParticleAPI in DaggerComponent


Open up the file **app/src/main/java/com.ndipatri.iot.zerioto98/ApplicationComponent.kt** 

>The ParticleAPI dependency is what communicates with our red siren's microcontroller.  You can see it gets its network connection from the OkHttpClient that is created here in the Dagger Component. 

>Let’s talk about ***how we can mock the network calls we make using this client***.  
>What we want to do is replace the OkHttpClient dependency you see here with our own modified version.  One that can provide mock network responses.  
>The best way to ***do this is with DaggerMock*** another open source library.

>With DaggerMock, we ***don’t have to modify our production code***.  We’re going to create a special Espresso ***TestRule*** that will swap out Dagger dependencies directly as needed while running our tests.


## Step 5 - Using DaggerMock with Unmocked network calls


### 5_1 - Create DaggerMock TestRule (live template 'step5_1_createRule')

Create new file **app/src/androidTest/java/com/ndipatri/iot/zeroto98/ApplicationComponentTestRule.kt** 

Type the following content:

```kotlin
class ApplicationComponentTestRule: DaggerMockRule<ApplicationComponent>(

   // The Dagger component whose dependencies we are mocking
   ApplicationComponent::class.java,

   // The modules that can have their dependencies overridden
   ApiModule()) {


}
```

>We’re **not going to be overriding everything provided by our Dagger*** component, just the OkHttpClient dependency.

Insert Dagger component's builder and OkHttpClient provider as shown:

```kotlin
class ApplicationComponentTestRule: DaggerMockRule<ApplicationComponent>(

   // The Dagger component whose dependencies we are mocking
   ApplicationComponent::class.java,

   // The modules that can have their dependencies overridden
   ApiModule()) {


   {insert live template step5_1}


}
```

Add **okHttpClient** to the constructor like this:

```kotlin
class ApplicationComponentTestRule(okHttpClient: OkHttpClient) :
```

>We’ve built and configured our DaggerMockRule, ***let’s see how we use this DaggerMockRule*** inside of our Espresso test to inject our modified OkHttpClient


### 5_2 - Use DaggerMockRule in our test (live template 'step5_2_useDaggerMockRule')

Open file **app/src/androidTest/java/com/ndipatri/iot/zeroto98/ExampeInstrumentedTest.kt** and insert live template as shown:

```kotlin
class MainActivityEspressoTest : TestCase() {

    {insert live template step5_2}

    @get:Rule
    val activityTestRule = ActivityTestRule(
       MainActivity::class.java,
       false,
       false
    )
```

>This componentRule will run for every test, ***swapping out the Dagger component with the modified one provided by DaggerMock at runtime***.  >The Component and all of its dependencies will be the same as in production with the exception of the OkHttpClient which we are passing into our DaggerMockRule.

>Notice we’ve configured our OkHttpClient with a ***MockInterceptor, yet another open source library contribution***. So we’re using a real implementation of Retrofit and OkHttpClient, but we’re intercepting the network requests just before they go out over the wire and returning mock data.  
>***We haven’t configured the MockInterceptor yet***, so let’s so how this test runs at this point.


***Uninstall the app before running test.***


Run the test and show the Logger output where MockInterceptor is complaining because we have a network call that hasn’t been configured. 

>All network calls that we are making during the test have to be explicitly mocked or they will cause an ***AssertionError by the MockInterceptor***

>This is actually how I recommend you use MockInterceptor: ***only mock the network calls that are actually made during the test!*** >Remember, it’s possible we’re building this test and we still don’t fully understand our application. So we let MockInterceptor tell us what calls we haven't mocked.

>Now lets go back and properly configure our MockInterceptor so our test will pass!


## Step 6 - Configure MockInterceptor


### 6_1 - Configure MockInterceptor (live template 'step6_1_configureMockInterceptor')

Open the file **app/src/androidTest/java/com/ndipatri/iot/zeroto98/ExampeInstrumentedTest.kt** and insert the live template code as shown:

```kotin
@Test
fun showCurrentRedSirenState_off() {

   {insert live template step6_1}

   activityTestRule.launchActivity(Intent())

```

>As usual, we define our mocks before we launch our Activity.

>Our test makes ***two calls to the Particle Cloud***.  One gets the current siren state and other turns the siren on.  So we have to mock both of these responses.  

>Our MockInterceptor Rule matches on the path of the outbound network request.

>The ***SirenStateResponse*** is what we get back from Retrofit in response to our network call.  This is how we configure our MockInterceptor Rule’s response as well.  We do have to convert it to JSON, however, before passing to the Rule.

>The last thing we can do before running this test is ***remove the ‘button.click()’*** view action.  
>Remember, we did this as a means of getting our live system to a known state.  
>Since we using mocks, we are disconnected from the live system and ***no longer have to do this***.

>The ‘UI polling’ feature of Kapsresso is no longer necessary, but we keep using it for other features it provides.

Run the test to show that we now have an end-to-end test of our application with the external dependency mocked.









