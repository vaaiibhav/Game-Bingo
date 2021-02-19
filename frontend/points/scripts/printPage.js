

    function setPrint () {
        this.contentWindow.__container__ = this;
        this.contentWindow.onbeforeunload = closePrint;
        this.contentWindow.onafterprint = closePrint;
        this.contentWindow.focus(); // Required for IE
        this.contentWindow.print();
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
     
      function printPage () {
        document.getElementById("invoice-POS").style.visibility='visible' ;
  
        document.getElementById("date").innerHTML = formatAMPM();
        document.getElementById("printgameid").innerHTML = 'ID: '+newgameid;
        // <td class="Rate" id="Rate"></td>
        //           <td class="payment" id="payment"></td>
        var totalbamoun = document.getElementById("totalbetamount").innerHTML = totalbet;
        var table = document.getElementById("betinTable");
  
       
          //if(boxnow[a]> 0){
            // document.getElementById("Rate").innerHTML = a+' X ';
            // document.getElementById("payment").innerHTML = boxnow[a];
            a=0;
            while(a<10)  {
             
            var row = table.insertRow(0);
            var cell1 = row.insertCell(0);
            cell1.style.width = '160px';
            // var cell2 = row.insertCell(1);
            if(Number(boxnow[a]) > 5){
            cell1.innerHTML = a +' X ' +boxnow[a];
            }
            // cell2.innerHTML =  ; 
            console.log('row created: '+a);
            a++;
            
            }
          //}
                  
        printJS('invoice-POS', 'html');
           document.getElementById("invoice-POS").style.visibility='hidden' ;
        // var printcontent = document.getElementById("printableArea").innerHTML;
  
  
  
        /////
        userBal = currentBal;
        boxall= boxall.SumArray(boxnow);//sumArray(boxall,boxnow);
        zeromaker();
        console.log('boxall: '+boxall);
        a=0;
            while(a<10)  {
            table.deleteRow(0);
            }
  
      }