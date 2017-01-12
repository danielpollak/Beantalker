# Beantalker
little app that can communicate with LightBlue Beans. Pairing with Android allows the bean to become a smartwatch or (coming soon!) an infrared remote. Or both.


## Arduino
Upload to the LightBlue Bean the beandroid.ino sketch in hamdanspam/Beantalker/beandroid/.


## Android
The beantalker app is in the folder named, conveniently, hamdanspam/Beantalker/beantalker. You can load it in android studio and upload it to your phone from there, or use any other programmer available.

## How to use it
I knitted a little case for my bean and enclosed my bean with it around an extremely cheap [perlon](http://www.primermagazine.com/2016/spend/a-comprehensive-guide-to-watch-straps) watch strap seen here:

<img src="https://github.com/hamdanspam/Beantalker/blob/master/img1.jpg" width=350>
<img src="https://github.com/hamdanspam/Beantalker/blob/master/img2.jpg" width=350>
<img src="https://github.com/hamdanspam/Beantalker/blob/master/img3.jpg" width=350>
<img src="https://github.com/hamdanspam/Beantalker/blob/master/img4.jpg" width=350>

I don't know knitting lingo too well, but I used 2 mm double sided needles, casted on 9 stitches (I shoulda used less, use 7 or 8.), and knitted it in the round for a good 10 rows, then switched to normal knitting, allowing me to go from a tube shape to a flat shape, which is exactly what I needed to secure the bean to the watch strap with buttons. I realized early on that I could get away with knitting in the round with only two needles, which seemed apocryphal at the time but the end product was more just better knitting overall, so I highly recommend it. Use relatively thin yarn, this is about where my knowledge of knitting fails me for more precise instructions.

Rotate your wrist to the left, rotate to the right. This activates communication between your phone and the bean, and the bean parses time data from the phone and displays it as a set of blinks in the following way:

### Hours
<table>
  <tr>
    <th>blink #</th><td>first</td><td>second</td>
  </tr>
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
    <th>blink #</th><td>third</td><td>fourth</td><td>fifth</td>
  </tr>
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

Hope that makes sense. I make a color coded post it based on the tables above and carried it with me for a while. Enjoy flaunting your masculinity, I know I am enjoying it, with the help of my new knitted color watch.
