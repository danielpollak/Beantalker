//samples per listen 
#define samples 10 // at 20 Hz, 50ms sleep
//threshold for directionality
#define thres 200
#define brightconst 175



AccelerationReading accel;
int alx[11] = {0}; // samples + 1
int aly[11] = {0}; // 10 + 1
int alz[11] = {0}; // 11


// this is the mutable sequence
char passSequence[5] = {'l', 'r', 'l'};

void setup() {
    //once upright, signal 'go'
    for(int i = 0; i < 3; i++){
        Bean.setLedGreen(200);
        Bean.sleep(400);
        Bean.setLedGreen(0);
        Bean.sleep(400);
    }
    while(true){passGest();}
}

void getTime(){
  if(Bean.getConnectionState()){
    uint8_t buffer[1];
    buffer[0]=0xFF;
    Bean.setScratchData(2, buffer, 1);
    Bean.sleep(750);
    ScratchData scratch = Bean.readScratchData(1);
    int r, g, b;
    
    // hours
    int h = scratch.data[0];
    int h1 = h / 4; // can be 0, 1, 2
    int h2 = (h - h1 * 4) / 1; // can be 0, 1, 2, 3
    if(h1 == 0)     {r=1; g=0; b=0;}
    else if(h1 == 1){r=0; g=1; b=0;}
    else            {r=0; g=0; b=1;}
    Bean.setLed(r * brightconst, g * brightconst, b * brightconst);
    Bean.sleep(500);
    if(h2 == 0)     {r=1; g=1; b=1;}
    else if(h2 == 1){r=1; g=0; b=0;}
    else if(h2 == 2){r=0; g=1; b=0;}
    else            {r=0; g=0; b=1;}
    Bean.setLed(r * brightconst, g * brightconst, b * brightconst);
    Bean.sleep(500);
    Bean.setLed(0, 0, 0);
    
    // pause between hours and minutes
    Bean.sleep(500); 
    
    // minutes
    int m = scratch.data[1];//eg, m = 57
    int m1 = m / 12; // m1 = 57 / 12 = 4
    int m2 = (m-m1*12)/4; // m2 = (57 - 48)/4 = 9/4 = 2
    int m3 = (m-m1*12-m2*4)/1; // m3 = (57 - 48 - 8)/1 = 1/1 = 1
    if(m1 == 0)     {r=1; g=0; b=1;}  // P
    else if(m1 == 1){r=1; g=1; b=1;}  // W
    else if(m1 == 2){r=1; g=0; b=0;}  // R
    else if(m1 == 3){r=0; g=1; b=0;}  // G
    else            {r=0; g=0; b=1;}  // B
    Bean.setLed(r * brightconst, g * brightconst, b * brightconst);
    Bean.sleep(500);
    if(m2 == 0)     {r=1; g=0; b=0;}  // R
    else if(m2 == 1){r=0; g=1; b=0;}  // G
    else            {r=0; g=0; b=1;}  // B
    Bean.setLed(r * brightconst, g * brightconst, b * brightconst);
    Bean.sleep(500);
    if(m3 == 0)     {r=1; g=1; b=1;}  // W
    else if(m3 == 1){r=1; g=0; b=0;}  // R
    else if(m3 == 2){r=0; g=1; b=0;}  // G
    else            {r=0; g=0; b=1;}  // B
    Bean.setLed(r * brightconst, g * brightconst, b * brightconst);
    Bean.sleep(500);
    Bean.setLed(0, 0, 0);
    } //no else: no connection
}

//void getTime(){
//  if(Bean.getConnectionState()){
//    uint8_t buffer[1];
//    buffer[0]=0xFF;
//    Bean.setScratchData(2, buffer, 1);
//    Bean.sleep(750);
//    ScratchData scratch = Bean.readScratchData(1);
//    if(scratch.data[2]){ // bc its a 1 or 0, so its a boolean
//      //if morning
//      for(int i = 0; i < scratch.data[0]; i++){ 
//        Bean.setLed(100, 100, 100);
//        Bean.sleep(250);
//        Bean.setLed(0, 0, 0);
//        Bean.sleep(250);
//      }
//      Bean.sleep(500); // pause between hours and minutes
//      for(int i = 0; i < scratch.data[1]; i++){ 
//        Bean.setLed(50, 50, 50);
//        Bean.sleep(250);
//        Bean.setLed(0, 0, 0);
//        Bean.sleep(250);
//      }
//    } else {
//     for(int i = 0; i < scratch.data[0]; i++){ 
//       Bean.setLed(150, 25, 125);
//       Bean.sleep(250);
//       Bean.setLed(0, 0, 0);
//       Bean.sleep(250);
//      }
//     Bean.sleep(500); // pause between hours and minutes
//     for(int i = 0; i < scratch.data[1]; i++){ 
//       Bean.setLed(150, 25, 125);
//        Bean.sleep(250);
//        Bean.setLed(0, 0, 0);
//       Bean.sleep(250);
//      }
//    }
//  }else{
//    Bean.setLed(100, 100, 100);
//    Bean.sleep(250);
//    Bean.setLed(0, 0, 0);
//    Bean.sleep(250);
//  }
//}
//
//method for actually unlocking I could also copy the code into loop, that would do the trick
void passGest(){
    char heard;
    
    for(int i = 0; i < 3/*sizeof(passSequence) - 1*/; i++){
        //heard = listen();
        //wait for current change
        //while(listen() == heard){}
        // if it's changed, but to the wrong thing
        //can be upright
        heard = listen();
        if(heard == 'u' || heard == 'z' || (i == 0 && heard != passSequence[i]) ||heard == passSequence[i-1]) {
            i--;
        } else if(heard == passSequence[i]) {
            Bean.setLed(0, 0, 220);
            Bean.sleep(100);
            Bean.setLed(0, 0, 0);
            continue;
        } else {
            // resetting.
            i = 0;
            Bean.setLed(220, 0, 0);
            Bean.sleep(100);
            Bean.setLed(0, 0, 0);
        }
    // else, will just continue
    }
    getTime();
}
void loop() {
//    passGuest();
}

char listen(){
    accel = Bean.getAcceleration();
    for(int i=0; i<samples; i++){
        alx[i] = accel.xAxis;
        aly[i] = accel.yAxis;
        alz[i] = accel.zAxis;
        Bean.sleep(50); // 50/1000
    }
    if(left())
       return 'l';
    else if(right())
        return 'r';
    else if(upright())
        return 'u';
    else if(down())
        return 'd';
    else if(facing())
        return 'f';
    else if(away())
        return 'a';
    else
        return 'z';
    
}
boolean left(){
    for(int i=0; i<samples; i++){
        if(!(alx[i] > thres))
            return false;
    }
    return true;
}

boolean right(){
    for(int i=0; i<samples; i++){
        if(!(alx[i] < -thres))
            return false;
    }
    return true;
}

boolean upright(){
    for(int i=0; i<samples; i++){
        if(!(alz[i]  > thres))
            return false;
    }
    return true;
}

boolean down(){
    for(int i=0; i<samples; i++){
        if(!(alz[i]  < -thres))
            return false;
    }
    return true;
}

boolean facing(){
    for(int i=0; i<samples; i++){
        if(!(aly[i]  > thres))
            return false;
    }
    return true;
}

boolean away(){
    for(int i=0; i<samples; i++){
        if(!(aly[i] < -thres))
            return false;
    }
    return true;
}

