# snmp_lmx
Repositorio de app SNMP 



comandos
pc1:

ip 192.168.56.2 255.255.255.0 192.168.56.1
show ip
save

pc2:
ip 192.168.57.2 255.255.255.0 192.168.57.1

R1:
conf t
int f0/0
ip address 192.168.56.1 255.255.255.0
no shut
exit

int f0/1
ip address 192.168.1.57.1 255.255.255.0
no shut
exit
exit

copy running-config startup-config
wr


----configurar conexion con dispositivo fisico

https://www.youtube.com/watch?v=zVkN9SinvSk&ab_channel=NeriITNet

lectura escritura
snmp-server community [comunidad] RW 1

snmp-server location "Sala de servidores"
snmp-server contact "admin@empresa.com"

verificar configuracion
show running-config | include snmp

verificar traps
show snmp trap



Router# configure terminal
Router(config)# snmp-server community public RO
Router(config)# snmp-server community public RW
Router(config)# snmp-server location "Sala de servidores"
Router(config)# snmp-server contact "admin@empresa.com"
Router(config)# snmp-server host 192.168.1.100 public version 2c
Router(config)# end


--instalar y configurar snmp en linux

sudo apt install snmp
sudo apt install snmpd



---MAQUINA VIRTUAL
Add-VMHardDiskDrive -VMName "GNS3 VM" -Path "GNS3 VM-disk002.vhd"

Set-VMProcessor -VMName "GNS3 VM" -ExposeVirtualizationExtensions $true
Set-VMNetworkAdapter -VMName "GNS3 VM" -MacAddressSpoofing on


87008094
74F07DE7D01E 4 COPIAS