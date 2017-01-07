# Beantalker
little app that can communicate with LightBlue Beans. Pairing with Android allows the bean to become a smartwatch or (coming soon!) an infrared remote. Or both.


## Arduino
Upload to the LightBlue Bean the beandroid.ino sketch in hamdanspam/Beantalker/beandroid/.


## Android
The beantalker app is in the folder named, conveniently, hamdanspam/Beantalker/beantalker. You can load it in android studio and upload it to your phone from there, or use any other programmer available.

## How to use it
I knitted a little case for my bean and enclosed my bean on top of a cheap watch strap seen here:
<img src="http://www.thinkgeek.com/images/products/frontsquare/imnl_tesla_watch.jpg">

Rotate your wrist to the left, pause, rotate to the right, pause, and rotate to the left again. This activates communication between your phone and the bean, and the bean parses time data from the phone in the following way:

### Hours
<table>
  <tr>
    <th>W</th><td>  </td><td>+0</td>
  </tr>
  <tr>
    <th>R</th><td>+0</td><td>+1</td>
  </tr>
  <tr>
    <th>G</th><td>+4</td><td>+2</td>
  </tr>
  <tr>
    <th>B</th><td>+8</td><td>+3</td>
  </tr>
</table>

### Minutes
<table>
  <tr>
    <th>P</th><td>+0 </td><td>  </td><td>  </td>
  </tr>
  <tr>
    <th>W</th><td>+12</td><td>  </td><td>+0</td>
  </tr>
  <tr>
    <th>R</th><td>+24</td><td>+0</td><td>+1</td>
  </tr>
  <tr>
    <th>G</th><td>+36</td><td>+4</td><td>+2</td>
  </tr>
  <tr>
    <th>B</th><td>+48</td><td>+8</td><td>+3</td>
  </tr>
</table>
