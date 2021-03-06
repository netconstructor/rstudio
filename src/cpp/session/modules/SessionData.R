#
# SessionData.R
#
# Copyright (C) 2009-12 by RStudio, Inc.
#
# This program is licensed to you under the terms of version 3 of the
# GNU Affero General Public License. This program is distributed WITHOUT
# ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
# MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
# AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
#
#

.rs.addFunction( "formatDataColumn", function(x, len, ...)
{
   # first truncate if necessary
   if ( length(x) > len )
      length(x) <- len

   # now format
   format(x, trim = TRUE, justify = "none", ...)
})


.rs.addGlobalFunction("viewData", function(x, title)
{
  if (missing(title))
    title <- paste("Data:", deparse(substitute(x))[1])
  as.num.or.char <- function(x) {
    if (is.character(x))
      x
    else if (is.numeric(x)) {
      storage.mode(x) <- "double"
      x
    }
    else as.character(x)
  }
  x0 <- as.data.frame(x)
  x <- lapply(x0, as.num.or.char)
  rn <- row.names(x0)
  if (any(rn != seq_along(rn)))
    x <- c(list(row.names = rn), x)
  if (!is.list(x) || !length(x) || !all(sapply(x, is.atomic)) ||
    !max(sapply(x, length)))
    stop("invalid 'x' argument")
  invisible(.Call("rs_viewData", x, title))
})
