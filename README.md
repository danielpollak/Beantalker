# Beantalker
little app that can communicate with LightBlue Beans. Pairing with Android allows the bean to become a smartwatch or (coming soon!) an infrared remote. Or both.


## Arduino
Upload to the LightBlue Bean the beandroid.ino sketch in hamdanspam/Beantalker/beandroid/.


## Android
The beantalker app is in the folder named, conveniently, hamdanspam/Beantalker/beantalker. You can load it in android studio and upload it to your phone from there, or use any other programmer available.

## How to use it
I knitted a little case for my bean and enclosed my bean on top of a cheap watch strap seen here:
<img src="Beantalker/img1.jpg">
<img src="Beantalker/img2.jpg">
<img src="Beantalker/img3.jpg">
<img src="Beantalker/img4.jpg">


Rotate your wrist to the left, pause, rotate to the right, pause, and rotate to the left again. This activates communication between your phone and the bean, and the bean parses time data from the phone in the following way:

### Hours
<table>
  <tr>
    <th>W</th><td>+0</td><td>+0</td>
  </tr>
  <tr>
    <th>R</th><td>+4</td><td>+1</td>
  </tr>
  <tr>
    <th>G</th><td>+8</td><td>+2</td>
  </tr>
  <tr>
    <th>B</th><td>  </td><td>+3</td>
  </tr>
</table>

### Minutes
<table>
  <tr>
    <th>W</th><td>+0</td><td>+0</td><td>+0</td>
  </tr>
  <tr>
    <th>R</th><td>+12</td><td>+4</td><td>+1</td>
  </tr>
  <tr>
    <th>G</th><td>+24</td><td>+8</td><td>+2</td>
  </tr>
  <tr>
    <th>B</th><td>+36</td><td>  </td><td>+3</td>
  </tr>
  <tr>
    <th>P</th><td>+48</td><td>  </td><td>  </td>
  </tr>
</table>

So, if you get a reading that goes: Green Blue...Red White Blue, that means
Green = 8, Blue = 3, so 8 + 3 = 12
Red = 12, White = 0 (always), Blue = 3, so 12 + 0 + 3 = 15
12:15.
Hope that makes sense.
