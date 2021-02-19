
const io = require('socket.io-client'); 
const electron = require('electron');
const path = require('path');
const url = require('url');
// var  socket = io.connect('http://spinnergame.in:11621');
var  socket = io.connect('http://192.168.0.120:11619/');


process.env.NODE_ENV = 'development';

const {app, BrowserWindow, Menu, ipcMain} = electron;
var loginWindow;
var pointsWindow;
var gameWindow;
var uname,tempuname;
var counter, connectSocket=false;
var newGameid, winns;
// Listen for app to be ready
app.on('ready', function(){
 // Create new window
  loginWindow = new BrowserWindow({
    minWidth: 950,
    minHeight: 600,
    //icon: path.join(__dirname, '/assets/wheel.ico'),
    webPreferences: {
      nodeIntegration: true
  }

  });
  // Load html in window
  loginWindow.loadURL('file://' + __dirname + './frontend/opening/login.html');
  loginWindow.maximize() ;

   // Quit app when closed;
  loginWindow.on('closed', function(){
log = null;  });

  // Build menu from template
  const mainMenu = Menu.buildFromTemplate(mainMenuTemplate);
  // Insert menu
   Menu.setApplicationMenu(mainMenu);
});
//handle points transfer window
function openpointstwindow(uname){
  console.log("login to points uname=",uname)
  pointsWindow = new BrowserWindow({
    minWidth: 950,
    minHeight: 600,
    webPreferences: {
      nodeIntegration: true,
  },
    // frame: false,
    //icon: path.join(__dirname, '/assets/wheel.ico')
  });
  pointsWindow.loadURL(url.format({
    pathname: path.join(__dirname, './frontend/points/pointransfer.html'),
    protocol: 'file:',
    slashes:true
  }));
  pointsWindow.maximize();
  // gameWindow.alwaysOnTop();
  connectSocket =true;
  loginWindow.close();
 
  function loginEssential(){
    socket.emit('getlastenwin','');
  getbalance();
  pointsWindow.webContents.send('uname',uname);

  }
  pointsWindow.webContents.once('dom-ready', () => {
    loginEssential();
  });
  
  function getbalance(){
    var data = new Object();
    data.uname = uname;
    var jstring = JSON.stringify(data);
    socket.emit('getcoins',jstring);
  }
  socket.on('ucoins', function(data){
    var usercoins= data;
    console.log('coins = '+usercoins);
    pointsWindow.webContents.send ('ucoins',usercoins); 
  });
  ipcMain.on('upcoins',function(e, upcoins){
    console.log('newbalance: '+upcoins);
    socket.emit('updatecoins', upcoins);
    setTimeout(getbalance,1500);

  });
  ipcMain.on('getbalance',function(e,nodata){
    getbalance();
  });
  
  pointsWindow.on('close', function(){
  });
}
// Handle add item window
function creategameWindow(uname){
 console.log("uname at main window time=",uname)
  gameWindow = new BrowserWindow({
    minWidth: 950,
    minHeight: 600,
    webPreferences: {
      nodeIntegration: true,
  }
    // frame: false,
    //icon: path.join(__dirname, '/assets/wheel.ico')

  });

  gameWindow.loadURL(url.format({
    pathname: path.join(__dirname, './frontend/main/main.html'),
    protocol: 'file:',
    slashes:true
  }));
  gameWindow.maximize();
  // gameWindow.alwaysOnTop();
  connectSocket =true;
  pointsWindow.close();
  
  function loginEssential(){
    socket.emit('getlastenwin','');
  getbalance();
  console.log("uname at main window time at send=",uname)
  gameWindow.webContents.send('uname',uname);
  // gameWindow.webContents.send('newgameid',newgameid);

  }
  gameWindow.webContents.once('dom-ready', () => {
    loginEssential();
  });

  socket.on('timer',function(countdown){
    console.log('countdowner= '+countdown.countdown);
    counter = countdown.countdown;
    gameWindow.webContents.send('timer',counter);
  
    if(counter == 64){
      console.log('emiiting lastwins at ' + countdown.countdown );
      gameWindow.webContents.send('uname',uname);

      }
      if(counter== 115){
        
      socket.emit('getlastenwin','3');
      gameWindow.webContents.send('uname',uname);

      }
      if(counter == 20){
        gameWindow.webContents.send('uname',uname);

      }   
  });
  socket.on('joker',function(joker){
    gameWindow.webContents.send('joker', joker);

  });

  socket.on('lastwinsare',function(data){
    console.log("last 10 win:",data);
    gameWindow.webContents.send('lastwinsare', data);
  }) 
  socket.on('newgameid',function(data){
    console.log("gameid send by server=",data);
    newgameid = data;
    gameWindow.webContents.send('newgameid',newgameid);
  });
  
  socket.on('winno',function(data){
    console.log("winning no"+data);
    gameWindow.webContents.send ('winno',data);
  });
  socket.on('ucoins', function(data){
    var usercoins= data;
    console.log('coins = '+usercoins);
    gameWindow.webContents.send ('ucoins',usercoins); 
  });
  
  
  ipcMain.on('savegame',function(e,data){
    console.log('savegame: '+data );
    
    socket.emit('savegame', data);
  });
  // ipcRenderer.send('newbalance', bal);
  ipcMain.on('upcoins',function(e, upcoins){
    console.log('newbalance: '+upcoins);
    socket.emit('updatecoins', upcoins);
    setTimeout(getbalance,1500);

  });
  ipcMain.on('getbalance',function(e,nodata){
    getbalance();
  });
  function getbalance(){
    var data = new Object();
    data.uname = uname;
    var jstring = JSON.stringify(data);
    socket.emit('getcoins',jstring);
  }
  // console.log(gameWindow.webContents.getPrinters());

  // Handle garbage collection
  gameWindow.on('close', function(){
    gameWindow = null;
    pointsWindow=null;
    loginWindow=null;
    app.quit();
  });
  // ipcMain.on('printreceipt',function (e,receipt){
  //   thermal_printer.alignCenter();

  //   thermal_printer.println("Spinner Game");
  //   thermal_printer.drawLine();
  //   thermal_printer.println("बेटी पढ़ाओ, बेटी बचाओ");
  //   thermal_printer.drawLine();
  //   thermal_printer.println(formatAMPM);
  //   var rc = Json.parse(receipt);
  //   thermal_printer.println();
  //   thermal_printer.print(rc);
  //   thermal_printer.println("For AMusement  Only");
   
  //   printer.printDirect({
  //     data: thermal_printer.getBuffer(),
  //     printer: epson.name,
  //     type: "TEXT",
  //     success: function (job_id) {
  //       console.log('OK :' + job_id);
  //     },
  //     error: function (err) {
  //       console.error(err);
  //     }
  //   });
  // });
}

function formatAMPM() {
  var d = new Date(),
  minutes = d.getMinutes().toString().length == 1 ? '0'+d.getMinutes() : d.getMinutes(),
  hours = d.getHours().toString().length == 1 ? '0'+d.getHours() : d.getHours(),
  ampm = d.getHours() >= 12 ? 'pm' : 'am',
  months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
  days = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
  return days[d.getDay()]+' '+months[d.getMonth()]+' '+d.getDate()+' '+d.getFullYear()+' '+hours+':'+minutes+ampm;
  }
ipcMain.on('login',function( e,uobj) {
  console.log('loginid2: '+uobj);
  nowobj = JSON.parse(uobj);
  tempuname = nowobj.uname;console.log(nowobj.uname);
  socket.emit('login',uobj);
});
socket.on('logsuccess',function(response){
  var servresponse;
console.log( 'response' + response);
if(response){

if(response =='true'){
  uname = tempuname;
  console.log('tempuname = '+tempuname);

openpointstwindow(uname);

} else{ switch(response){
  
  case 'erroroc' : servresponse = 'errorc';break;
  case 'false': servresponse = 'false'; break;
  case 'noemail': servresponse = 'noemail'; break;
}
} 
}
else{
 servresponse = 'notcon'
}
// loginWindow.webContents.send('serverresponse', servresponse);



});
ipcMain.on('gamewin',function( e,mwindow) {
  console.log('mwindow: '+uname);
  try {
  if(mwindow=='true'){
    creategameWindow(uname);
  }
} catch(e) {
  console.log(e);
}
  
})

// Create menu template
const mainMenuTemplate =  [
  // Each object is a dropdown
  // {
  //   label: 'File',
  //   submenu:[
  //     {
  //       label:'Add Item',
  //       click(){
  //         creategameWindow();

  //       }
  //     },
  //     {
  //       label:'Clear Items',
  //       click(){
  //         loginWindow.webContents.send('item:clear');
  //       }
  //     },
  //     {
  //       label: 'Quit',
  //       accelerator:process.platform == 'darwin' ? 'Command+Q' : 'Ctrl+Q',
  //       click(){
  //         app.quit();
  //       }
  //     }
  //   ]
  // }
];

// If OSX, add empty object to menu
// if(process.platform == 'darwin'){
//   mainMenuTemplate.unshift({});
// }

// // Add developer tools option if in dev
if(process.env.NODE_ENV !== 'production'){
  mainMenuTemplate.push({
    label: 'Developer Tools',
    submenu:[
      {
        role: 'reload'
      },
      {
        label: 'Toggle DevTools',
        accelerator:process.platform == 'darwin' ? 'Command+I' : 'Ctrl+I',
        click(item, focusedWindow){
          focusedWindow.toggleDevTools();
        }
      }
    ]
  });
}