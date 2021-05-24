int SIREN = D7; 
int BUTTON = D4; 

void setup() {

    pinMode(SIREN, OUTPUT);
    pinMode(BUTTON, INPUT_PULLUP);

    // Remote Particle Cloud clients can make functions calls against this device
    // while it is connected.
    Particle.function("sirenOn", sirenOn);
    Particle.function("sirenOff", sirenOff);

    Particle.variable("sirenState", getCurrentSirenState);
}

long lastToggleTimeMillis = millis();
void loop() {

    if (digitalRead(BUTTON) == HIGH) {
        if ((millis() - lastToggleTimeMillis > 500)) {
            toggleSiren();
            lastToggleTimeMillis = millis();
        }
    }

    delay(100); // save some trees
}

void toggleSiren() {
    if (digitalRead(SIREN) == HIGH) {
        digitalWrite(SIREN, LOW);
    } else {
        digitalWrite(SIREN, HIGH);
    }
}

int sirenOn(String _ignored) {
    // NJD this delay is intentional.. for testing purposes
    delay(2000);

    digitalWrite(SIREN, HIGH);
    return 1;
}

int sirenOff(String _ignored) {
    // NJD this delay is intentional.. for testing purposes
    //delay(1000);

    digitalWrite(SIREN, LOW);
    return 1;
}

char* getCurrentSirenState() {
   if (digitalRead(SIREN)) {
        return "on";
   } else {
        return "off";
   }
}