default file = FLUX_DIR + "10.marc21";

file|
open-file|
as-lines|
decode-marc21|
morph(FLUX_DIR + "morph-marc21.xml")|
encode("literals")|
write("stdout");
//write("file://???");