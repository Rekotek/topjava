function adjustTwoChars(n) {
    return (n === 0) ? "00" : ((n < 10) ? ('0' + n) : n)
}
function convertToISODateTime(d) {
    let yearStr = '' + d.getFullYear();
    let month = (d.getMonth() + 1);
    let monthStr = (month < 10) ? ('0' + month) : ('' + month);
    let dayStr = ((d.getDate() < 10) ? '0' : '') + d.getDate();
    let hour = d.getHours();
    let hourStr = adjustTwoChars(hour);
    let minutes = d.getMinutes();
    let minutesStr = adjustTwoChars(minutes);
    return `${yearStr}-${monthStr}-${dayStr}T${hourStr}:${minutesStr}`;
}
