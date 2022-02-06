#!/bin/sed

# 1 here is keyword, 2 is category
# 3 (which we're going to look up with this sql statement/creation)
# is the tables that the keyword is in (we don't want to update all of them... it may be a waste of time... even though... if this fails... maybe we should
#just do that!

s/^([^,]*),(.*)$/select "\1", "\2", source from BigTXView where description like "%\1%" order by source asc;\nselect "~" from BigTXView where 1=1 limit 1;\n/

#s/^([^,]*),(.*)$/\2,\1/
