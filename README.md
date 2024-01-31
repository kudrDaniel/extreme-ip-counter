# :clipboard:Overview
IP address counter - console program to counting unique ip-addresses in text file. This is a solution of [this task](https://github.com/Ecwid/new-job/blob/master/IP-Addr-Counter.md).

## :memo:Status
|    Test     |    Time     |     Memory     |
|:-----------:|:-----------:|:--------------:|
| In progress | ~20 min[^1] | 512-768 mb[^2] |

[^1]: Execution time on [test file](https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip) that unzipped size ~120 Gb
[^2]: Average ram consumption on any file

## :file_folder:How to use
1. `git clone https://github.com/kudrDaniel/extreme-ip-counter.git`
1. `gradlew installDist` in the root of project
1. `build\install\extreme-ip-counter\bin\extreme-ip-counter [file path]`

### :loudspeaker:Requirements
|  Name  | Version |
|:------:|:-------:|
|  Java  |  > 20   |
| Gradle |  > 8.4  |